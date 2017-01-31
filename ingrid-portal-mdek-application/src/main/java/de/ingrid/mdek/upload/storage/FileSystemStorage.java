package de.ingrid.mdek.upload.storage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * FileSystemStorage manages files in the server file system
 */
public class FileSystemStorage implements Storage {

	private final static Logger log = Logger.getLogger(FileSystemStorage.class);

	private String docsDir = null;
	private String partsDir = null;

	/**
	 * Constructor
	 * @param docsDir
	 * @param partsDir
	 */
	public FileSystemStorage(String docsDir, String partsDir) {
		this.docsDir = docsDir;
		this.partsDir = partsDir;
	}

	@Override
	public String[] list(String path) throws IOException {
		Path realPath = this.getRealPath(path, this.docsDir);
        List<String> files = new ArrayList<String>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(realPath)) {
            for (Path entry: stream) {
            	if (Files.isRegularFile(entry)) {
            		files.add(this.stripPath(entry.toString()));
            	}
            }
        }
        return files.toArray(new String[files.size()]);
    }

	@Override
	public boolean exists(String path) {
		Path realPath = this.getRealPath(path, this.docsDir);
		return Files.exists(realPath);
	}

	@Override
	public boolean isDirectory(String path) {
		Path realPath = this.getRealPath(path, this.docsDir);
		return Files.isDirectory(realPath);
	}

	@Override
	public InputStream read(String file) throws IOException {
		Path realPath = this.getRealPath(file, this.docsDir);
		return Files.newInputStream(realPath);
	}

	@Override
	public String[] write(String path, String file, InputStream data, Integer size, boolean replace) throws IOException {
		Path filePath = Paths.get(path, file);
		Path realPath = this.getRealPath(this.sanitize(filePath.toString()), this.docsDir);
		Files.createDirectories(realPath.getParent());

		// copy file
		List<CopyOption> copyOptionList = new ArrayList<CopyOption>();
		if (replace) {
			copyOptionList.add(StandardCopyOption.REPLACE_EXISTING);
		}
		CopyOption[] copyOptions = copyOptionList.toArray(new CopyOption[copyOptionList.size()]);

		Files.copy(data, realPath, copyOptions);
		if (Files.size(realPath) != size) {
			throw new IOException("The file size is different to the expected size");
		}
		String[] result = new String[] { realPath.toString() };

		// extract archives
		try {
			String contentType = Files.probeContentType(realPath);
			if (contentType.contains("zip") || contentType.contains("compressed")) {
				result = this.uncompress(realPath, copyOptions);
				// delete archive
				Files.delete(realPath);
			}
		} catch (Exception ex) {
			log.error("Post processing of '"+realPath+"' failed due to the following exception:\n"+ex.toString());
		}

		// prepare result
		for (int i=0, count=result.length; i<count; i++) {
			result[i] = this.stripPath(result[i]);
		}
		return result;
	}


	@Override
	public void writePart(String id, Integer index, InputStream data, Integer size) throws IOException {
		String file = id+"-"+index;
		Path realPath = this.getRealPath(file, this.partsDir);
		Files.createDirectories(realPath.getParent());
		Files.copy(data, realPath, StandardCopyOption.REPLACE_EXISTING);
		if (Files.size(realPath) != size) {
			throw new IOException("The file size is different to the expected size");
		}
	}

	@Override
	public String[] combineParts(String path, String file, String id, Integer totalParts, Integer size, boolean replace)
			throws IOException {
		// combine parts into stream
		Vector<InputStream> streams = new Vector<InputStream>();
		Path[] parts = new Path[totalParts];
		for (int i=0; i<totalParts; i++) {
			String part = id+"-"+i;
			Path realPath = this.getRealPath(part, this.partsDir);
			streams.add(Files.newInputStream(realPath));
			parts[i] = realPath;
		}

		// delegate to write method
		InputStream data = new SequenceInputStream(streams.elements());
		String[] files = this.write(path, file, data, size, replace);

		// delete parts
		for (Path part : parts) {
			Files.delete(part);
		}
		return files;
	}

	@Override
	public void delete(String file) throws IOException {
		Path realPath = this.getRealPath(file, this.docsDir);
		Files.delete(realPath);
	}

	@Override
	public void alias(String oldPath, String newPath) throws IOException {
		// TODO create a symbolic link
	}

	/**
	 * Uncompress the file denoted by path
	 * @param path
	 * @param copyOptions
	 * @return String[]
	 * @throws Exception
	 */
	private String[] uncompress(Path path, CopyOption... copyOptions) throws Exception {
		List<Path> result = new ArrayList<Path>();
		try (InputStream fis = FileUtils.openInputStream(path.toFile());
				InputStream bis = new BufferedInputStream(fis)) {
			InputStream bcis = null;
			try {
				bcis = new BufferedInputStream(new CompressorStreamFactory().createCompressorInputStream(bis));
			} catch (CompressorException e) {
				// a compressor was not recognized in the stream, in this case
				// we leave the inputStream as-is
			}

			InputStream is = bcis != null ? bcis : bis;
			try (ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(is)) {
				ArchiveEntry entry = ais.getNextEntry();
				Path parent = path.getParent();
				while (entry != null) {
					Path file = Paths.get(this.sanitize(parent.toString()), this.sanitize(entry.getName()));
					if (entry.isDirectory()) {
						// handle directory
						Files.createDirectories(file);
					} else {
						// handle file
						Files.copy(ais, file, copyOptions);
						result.add(file);
					}
					entry = ais.getNextEntry();
				}
			}
			catch (Exception ex) {
				log.error("Failed to uncompress '"+path+"'. Cleaning up...");
				// delete all extracted files, if one file fails
				for (Path file : result) {
					try {
						Files.delete(file);
					}
					catch (Exception ex1) {
						log.error("Could not delete '"+file+"' while cleaning up from failed uncompressing.");
					}
				}
				throw ex;
			}
		}
		return result.stream().map(p -> p.toString()).toArray(size -> new String[size]);
	}

	/**
	 * Replace forbidden characters from a path
	 * @param path
	 * @return String
	 */
	private String sanitize(String path) {
		// TODO remove forbidden characters
		return path;
	}

	/**
	 * Get the real path of a requested path
	 * @param file
	 * @param basePath
	 * @return Path
	 */
	private Path getRealPath(String file, String basePath) {
		return FileSystems.getDefault().getPath(basePath, file);
	}

	/**
	 * Remove the upload base directory from a path
	 * @param path
	 * @return String
	 */
	private String stripPath(String path) {
		Path basePath = FileSystems.getDefault().getPath(this.docsDir);
		return path.replace(basePath.toString(), "");
	}
}
