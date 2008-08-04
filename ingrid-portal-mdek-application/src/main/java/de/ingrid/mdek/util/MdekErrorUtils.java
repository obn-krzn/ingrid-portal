package de.ingrid.mdek.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.ingrid.mdek.DataMapperInterface;
import de.ingrid.mdek.MdekError;
import de.ingrid.mdek.MdekKeys;
import de.ingrid.mdek.MdekKeysSecurity;
import de.ingrid.mdek.MdekError.MdekErrorType;
import de.ingrid.mdek.beans.address.MdekAddressBean;
import de.ingrid.mdek.beans.object.MdekDataBean;
import de.ingrid.mdek.exception.EntityReferencedException;
import de.ingrid.mdek.exception.GroupDeleteException;
import de.ingrid.mdek.exception.InvalidPermissionException;
import de.ingrid.mdek.job.MdekException;
import de.ingrid.mdek.job.repository.IJobRepository;
import de.ingrid.utils.IngridDocument;

public class MdekErrorUtils {

	private final static Logger log = Logger.getLogger(MdekErrorUtils.class);

	// Injected via Spring
	private static DataMapperInterface dataMapper;


	public static void handleError(IngridDocument response) throws RuntimeException {
		String errorMessage = getErrorMsgFromResponse(response);
		log.debug(errorMessage);
		List<MdekError> err = getErrorsFromResponse(response);
		if (err != null) {
			if (containsErrorType(err, MdekErrorType.ENTITY_REFERENCED_BY_OBJ)) {
				handleEntityReferencedByObjectError(err, MdekErrorType.ENTITY_REFERENCED_BY_OBJ);
			
			} else if (containsErrorType(err, MdekErrorType.ADDRESS_IS_AUSKUNFT)) {
				handleEntityReferencedByObjectError(err, MdekErrorType.ADDRESS_IS_AUSKUNFT);

			} else if (containsErrorType(err, MdekErrorType.SINGLE_BELOW_TREE_OBJECT_PERMISSION)
					|| containsErrorType(err, MdekErrorType.TREE_BELOW_TREE_OBJECT_PERMISSION)
					|| containsErrorType(err, MdekErrorType.SINGLE_BELOW_TREE_ADDRESS_PERMISSION)
					|| containsErrorType(err, MdekErrorType.TREE_BELOW_TREE_ADDRESS_PERMISSION)
					|| containsErrorType(err, MdekErrorType.USER_EDITING_OBJECT_PERMISSION_MISSING)
					|| containsErrorType(err, MdekErrorType.USER_RESPONSIBLE_FOR_OBJECT_PERMISSION_MISSING)
					|| containsErrorType(err, MdekErrorType.USER_EDITING_ADDRESS_PERMISSION_MISSING)
					|| containsErrorType(err, MdekErrorType.USER_RESPONSIBLE_FOR_ADDRESS_PERMISSION_MISSING)
					|| containsErrorType(err, MdekErrorType.NO_RIGHT_TO_REMOVE_ADDRESS_PERMISSION)
					|| containsErrorType(err, MdekErrorType.NO_RIGHT_TO_REMOVE_OBJECT_PERMISSION)
					|| containsErrorType(err, MdekErrorType.NO_RIGHT_TO_REMOVE_USER_PERMISSION)
					|| containsErrorType(err, MdekErrorType.NO_RIGHT_TO_ADD_USER_PERMISSION)) {
				handleInvalidPermissionError(err);

			} else if (containsErrorType(err, MdekErrorType.GROUP_HAS_USERS)) {
				handleGroupHasUsersError(err);

			} else {
				throw new MdekException(err);
			}
		} else if (errorMessage != null){
			throw new RuntimeException(errorMessage);
		} else {
			return;
		}
	}

	private static void handleEntityReferencedByObjectError(List<MdekError> errorList, MdekErrorType errType) {
		MdekAddressBean targetAddress = null;
		MdekDataBean targetObject = null;
		ArrayList<MdekAddressBean> sourceAddresses = new ArrayList<MdekAddressBean>();
		ArrayList<MdekDataBean> sourceObjects = new ArrayList<MdekDataBean>();

		for (MdekError mdekError : errorList) {
			if (mdekError.getErrorType().equals(errType)) {
				// In the case of this exception, we have to build an MdekAppException containing additional data
				IngridDocument errorInfo = mdekError.getErrorInfo();
				ArrayList<MdekDataBean> objs = MdekObjectUtils.extractDetailedObjects(errorInfo);
				ArrayList<MdekAddressBean> adrs = MdekAddressUtils.extractDetailedAddresses(errorInfo);
				sourceObjects.addAll(objs);
				sourceAddresses.addAll(adrs);

				targetAddress = dataMapper.getDetailedAddressRepresentation(errorInfo);
			}
		}

		EntityReferencedException e = new EntityReferencedException(errType.toString());
		e.setTargetAddress(targetAddress);
		e.setTargetObject(targetObject);
		e.setSourceAddresses(sourceAddresses);
		e.setSourceObjects(sourceObjects);
		throw e;
	}

	private static void handleInvalidPermissionError(List<MdekError> errorList) {
		MdekAddressBean rootAddress = null;
		MdekDataBean rootObject = null;
		MdekAddressBean invalidAddress = null;
		MdekDataBean invalidObject = null;
		MdekErrorType errorType = null;
		
		for (MdekError mdekError : errorList) {
			MdekErrorType err = mdekError.getErrorType();
			if (err.equals(MdekErrorType.SINGLE_BELOW_TREE_OBJECT_PERMISSION)
				|| err.equals(MdekErrorType.TREE_BELOW_TREE_OBJECT_PERMISSION)
				|| err.equals(MdekErrorType.SINGLE_BELOW_TREE_ADDRESS_PERMISSION)
				|| err.equals(MdekErrorType.TREE_BELOW_TREE_ADDRESS_PERMISSION)) {
				// In the case of this exception, we have to build an MdekAppException containing additional data
				errorType = err;
				IngridDocument errorInfo = mdekError.getErrorInfo();
				ArrayList<MdekAddressBean> adrs = MdekAddressUtils.extractDetailedAddresses(errorInfo);
				ArrayList<MdekDataBean> objs = MdekObjectUtils.extractDetailedObjects(errorInfo);
				if (adrs != null && adrs.size() == 2) {
					rootAddress = adrs.get(0);
					invalidAddress = adrs.get(1);
				}
				if (objs != null && objs.size() == 2) {
					rootObject = objs.get(0);
					invalidObject = objs.get(1);
				}
				// Only handle the first exception that is recognized
				break;

			} else if (err.equals(MdekErrorType.USER_EDITING_OBJECT_PERMISSION_MISSING)
					|| err.equals(MdekErrorType.USER_RESPONSIBLE_FOR_OBJECT_PERMISSION_MISSING)) {
				errorType = err;
				IngridDocument errorInfo = mdekError.getErrorInfo();
				errorInfo.put(MdekKeys.ADR_ENTITIES, errorInfo.remove(MdekKeysSecurity.USER_ADDRESSES));
				ArrayList<MdekAddressBean> adrs = MdekAddressUtils.extractDetailedAddresses(errorInfo);
				ArrayList<MdekDataBean> objs = MdekObjectUtils.extractDetailedObjects(errorInfo);
				rootAddress = adrs.get(0);
				invalidObject = objs.get(0);
				break;				

			} else if (err.equals(MdekErrorType.USER_EDITING_ADDRESS_PERMISSION_MISSING)
					|| err.equals(MdekErrorType.USER_RESPONSIBLE_FOR_ADDRESS_PERMISSION_MISSING)) {
				errorType = err;
				IngridDocument errorInfo = mdekError.getErrorInfo();
				ArrayList<MdekAddressBean> invalidAdrs = MdekAddressUtils.extractDetailedAddresses(errorInfo);
				errorInfo.put(MdekKeys.ADR_ENTITIES, errorInfo.remove(MdekKeysSecurity.USER_ADDRESSES));
				ArrayList<MdekAddressBean> userAdrs = MdekAddressUtils.extractDetailedAddresses(errorInfo);
				rootAddress = userAdrs.get(0);
				invalidAddress = invalidAdrs.get(0);
				break;

			} else if (err.equals(MdekErrorType.NO_RIGHT_TO_REMOVE_OBJECT_PERMISSION)
					|| err.equals(MdekErrorType.NO_RIGHT_TO_REMOVE_ADDRESS_PERMISSION)) {
				errorType = err;
				IngridDocument errorInfo = mdekError.getErrorInfo();
				ArrayList<MdekAddressBean> adrs = MdekAddressUtils.extractDetailedAddresses(errorInfo);
				ArrayList<MdekDataBean> objs = MdekObjectUtils.extractDetailedObjects(errorInfo);

				if (adrs != null && adrs.size() > 0) {
					rootAddress = adrs.get(0);
				}
				if (objs != null && objs.size() > 0) {
					rootObject = objs.get(0);
				}
				break;

			} else if (err.equals(MdekErrorType.NO_RIGHT_TO_REMOVE_USER_PERMISSION)
					|| err.equals(MdekErrorType.NO_RIGHT_TO_ADD_USER_PERMISSION)) {
				errorType = err;
				break;
			}
		}

		InvalidPermissionException e = new InvalidPermissionException(errorType.toString());
		e.setRootAddress(rootAddress);
		e.setRootObject(rootObject);
		e.setInvalidAddress(invalidAddress);
		e.setInvalidObject(invalidObject);
		throw e;
	}

	private static void handleGroupHasUsersError(List<MdekError> errorList) {
		ArrayList<MdekAddressBean> addresses = new ArrayList<MdekAddressBean>();
		MdekErrorType errorType = null;
		
		for (MdekError mdekError : errorList) {
			MdekErrorType err = mdekError.getErrorType();
			if (err.equals(MdekErrorType.GROUP_HAS_USERS)) {
				// In the case of this exception, we have to build an MdekAppException containing additional data
				errorType = err;
				IngridDocument errorInfo = mdekError.getErrorInfo();
				errorInfo.put(MdekKeys.ADR_ENTITIES, errorInfo.remove(MdekKeysSecurity.IDC_USERS));
				addresses = MdekAddressUtils.extractDetailedAddresses(errorInfo);
				// Only handle the first exception that is recognized
				break;
			}
		}

		GroupDeleteException e = new GroupDeleteException(errorType.toString());
		e.setAddresses(addresses);
		throw e;
	}
	
	private static boolean containsErrorType(List<MdekError> errorList, MdekErrorType errorType) {
		for (MdekError mdekError : errorList) {
			if (mdekError != null && mdekError.getErrorType().equals(errorType))
				return true;
		}
		return false;
	}
	
	private static List<MdekError> getErrorsFromResponse(IngridDocument mdekResponse) {
		return (List<MdekError>) mdekResponse.get(IJobRepository.JOB_INVOKE_ERROR_MDEK);
	}

	private static String getErrorMsgFromResponse(IngridDocument mdekResponse) {
		int numErrorTypes = 4;
		String[] errMsgs = new String[numErrorTypes];

		errMsgs[0] = (String) mdekResponse.get(IJobRepository.JOB_REGISTER_ERROR_MESSAGE);
		errMsgs[1] = (String) mdekResponse.get(IJobRepository.JOB_INVOKE_ERROR_MESSAGE);
		errMsgs[2] = (String) mdekResponse.get(IJobRepository.JOB_COMMON_ERROR_MESSAGE);
		errMsgs[3] = (String) mdekResponse.get(IJobRepository.JOB_DEREGISTER_ERROR_MESSAGE);

		String retMsg = null;
		for (String errMsg : errMsgs) {
			if (errMsg != null) {
				if (retMsg == null) {
					retMsg = errMsg;
				} else {
					retMsg += "\n!!! Further Error !!!:\n" + errMsg;
				}
			}
		}
		
		return retMsg;
	}

	public static RuntimeException convertToRuntimeException(MdekException e) {
		String errorStr = "";
		List<MdekError> errorList = e.getMdekErrors();
		for (MdekError mdekError : errorList) {
			errorStr += mdekError.toString()+" ";
		}
		return new RuntimeException(errorStr.trim());
	}

	public DataMapperInterface getDataMapper() {
		return dataMapper;
	}

	public void setDataMapper(DataMapperInterface dataMapper) {
		MdekErrorUtils.dataMapper = dataMapper;
	}

}
