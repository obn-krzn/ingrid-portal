/*
 * **************************************************-
 * InGrid Portal MDEK Application
 * ==================================================
 * Copyright (C) 2014 - 2022 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
define({
    "dialog.simulation.parameter.title": "Simulationsparameter/-Größe",
    "dialog.lfs.link.parameter.title": "Links zum Langfristspeicher",
    "dialog.lfs.move.warn": "Achtung: die Daten werden hiermit in den Langfristspeicher verschoben und können dort nicht mehr verändert oder gelöscht werden! Daher prüfen Sie bitte sorgfältig die Inhalte, Namen, etc. von Ihrer Datei / Ihrem Verzeichnis, bevor Sie auf Ok klicken.",

    "ui.obj.baw.auftragsnummer.title": "Auftragsnummer",
    "ui.obj.baw.auftragsnummer.help": "Auftragsnummer",
    "ui.obj.baw.auftragstitel.title": "Auftragstitel",
    "ui.obj.baw.auftragstitel.help": "Titel des BAW/WSV Auftrags",
    "ui.obj.baw.bwastr.table.column.km_end": "Km-bis",
    "ui.obj.baw.bwastr.table.column.km_start": "Km-von",
    "ui.obj.baw.bwastr.table.column.name": "BWaStr.-Streckenname",
    "ui.obj.baw.bwastr.table.help": "Streckenabschnitte der Bundeswasserstrassen nach VV/WSV 1103",
    "ui.obj.baw.bwastr.table.title": "Streckenabschnitte",

    "ui.obj.baw.hierarchy.level.name.title": "Bezeichnung der Hierarchieebene",
    "ui.obj.baw.hierarchy.level.name.help": "Bezeichnung der Hierarchieebene",
    "ui.obj.baw.keyword.catalogue.row.title": "Schlagwort",
    "ui.obj.baw.keyword.catalogue.table.help": "BAW-Schlagwortkatalog 2012",
    "ui.obj.baw.keyword.catalogue.table.title": "BAW-Schlagwortkatalog 2012",
    "ui.obj.baw.simulation.model.type.table.title": "Simulationsmodellart",
    "ui.obj.baw.simulation.model.type.table.help": "Art des Simulationsmodells",
    "ui.obj.baw.simulation.parameter.dialog.table.column.value": "Wert",
    "ui.obj.baw.simulation.parameter.table.title": "Simulationsparameter/-Größen",
    "ui.obj.baw.simulation.parameter.table.help": "Simulationsparameter/-Größen",
    "ui.obj.baw.simulation.parameter.table.column.name": "Name",
    "ui.obj.baw.simulation.parameter.table.column.role": "Rolle",
    "ui.obj.baw.simulation.parameter.table.column.value": "Wert/Wertebereich",
    "ui.obj.baw.simulation.parameter.table.column.units": "Maßeinheit",
    "ui.obj.baw.simulation.parameter.table.new.row": "Simulationsparameter/-Größe hinzufügen",
    "ui.obj.baw.simulation.process.title": "Simulationsverfahren",
    "ui.obj.baw.simulation.process.help": "Verfahren für die numersiche Simulation",
    "ui.obj.baw.simulation.timestep.title": "Zeitliche Genauigkeit",
    "ui.obj.baw.simulation.timestep.help": "Zeitschrittgröße der Simulation in Sekunden",
    "ui.obj.baw.simulation.spatial.dimensionality.title": "Räumliche Dimensionalität",
    "ui.obj.baw.simulation.spatial.dimensionality.help": "Räumliche Dimensionalität der Simulation",
    "ui.obj.baw.literature.xref.table.title": "Literaturverweise",
    "ui.obj.baw.literature.xref.table.help": "Literaturverweise hinzufügen",
    "ui.obj.baw.literature.xref.table.new.row": "Hinzufügen",
    "ui.obj.baw.literature.xref.table.edit.row": "Bearbeiten",
    "ui.obj.baw.literature.xref.table.new.row.tooltip": "Literaturverweis hinzufügen",
    "ui.obj.baw.literature.xref.table.row.title": "Literaturverweis",
    "ui.obj.baw.lfs.link.table.title": "Langfristspeicher",
    "ui.obj.baw.lfs.link.table.help": "Dateien in den Langfristspeicher verschieben oder Verweis zu einem bestehenden Verzeichnis/Datei im Langfristspeicher erstellen.<br><br>Verzeichnis/Datei in den Langfristspeicher verschieben und automatisch einen Link erzeugen:<br><ol><li>Prüfen Sie sorgfältig die Dateiinhalte und -Namen vom zu archivierenden Verzeichnis/Dateie.</li><li>Kopieren Sie im Dateiexplorer das Verzeichnis / die Dateie in einen von den Eingangsbereichen unter \\\\lfs-ka.all.baw.de\\Eingang oder \\\\lfs-hh.all.baw.de\\Eingang</li><li>Betätigen Sie den Link 'Hinzufügen'</li><li>Befüllen Sie das Formular in der Dialogbox und wählen Sie die im Schritt 2 kopierten Verzeichnisse/Dateien im Eingangsbereich aus.</li><li>Klicken Sie auf 'Hinzufügen'. Das Verzeichnis/Datei wird aus dem Eingangsbereich in den Langfristspeicher verschoben und ein Link automatisch in diese Tabelle eingetragen.</li></ol><br>Link zu einem bestehenden Verzeichnis /Datei im Langfristspeicher erzeugen:<br><ol><li>Klicken Sie auf den Link 'Hinzufügen'</li><li>Wählen Sie die Option 'LFS-Ablage' aus.</li><li>Befüllen Sie das Formular aus und wählen Sie eine Datei aus dem Langfristspeicher aus.</li><li>4. Klicken Sie auf 'Hinzufügen'. Ein Link zur ausgewählten Datei wird damit erzeugt und in diese Tabelle eingetragen.</li></ol><br>Link Eigenschaften bearbeiten:<br><ol><li>Klicken Sie mit der rechten Maustaste auf die zu bearbeitende Zeile.</li><li>Klicken Sie auf 'Zeile bearbeiten'.</li><li>Bearbeiten Sie die Einträge im Formular.</li><li>Klicken Sie auf 'Übernehmen'.</li></ol><br>Wichtige Hinweise:<br><ol><li>Nach den Verschieben in den Langfristspeicher sind keinerlei Änderungen möglich. Das gilt auch wenn eine Zeile in dieser Tabelle bearbeitet oder gelöscht wird oder dieser Metadatensatz gelöscht wird. Prüfen Sie deswegen die Dateiinhalte und -Namen sorgfältig, bevor Sie den Verschiebevorgang starten.</li><li>Der Verschiebevorgang erfolgt über einen Dienst, der unabhängig von diesem Metadateneditor läuft. Auch ohne Veröffentlichung des aktuellen Metadatensatz, sind nach dem Verschiben die Dateien im Langfristspeicher verfügbar und können nicht mehr gelöscht werden.</li></ol>",
    "ui.obj.baw.lfs.link.table.new.row": "Hinzufügen",
    "ui.obj.baw.lfs.link.table.edit.row": "Bearbeiten",
    "ui.obj.baw.lfs.link.table.new.row.tooltip": "Einträge können erst dann hinzugefügt werden, wenn die Felder Auftragsnummer und Streckenabschnitte ausgefüllt wurden.",
    "ui.obj.baw.lfs.link.table.column.link": "Link",
    "ui.obj.baw.lfs.link.table.column.name": "Name",
    "ui.obj.baw.lfs.link.table.column.fileFormat": "Dateiformat",
    "ui.obj.baw.links.to": "Andere Verweise",
    "ui.obj.baw.measuring.method.title": "Messverfahren",
    "ui.obj.baw.measuring.method.help": "",
    "ui.obj.baw.measuring.spatiality.title": "Räumlichkeit",
    "ui.obj.baw.measuring.spatiality.help": "",
    "ui.obj.baw.measuring.measuringDepth.title": "Messtiefe",
    "ui.obj.baw.measuring.measuringDepth.help": "",
    "ui.obj.baw.measuring.unitOfMeasurement.title": "Maßeinheit",
    "ui.obj.baw.measuring.unitOfMeasurement.help": "",
    "ui.obj.baw.measuring.heightReferenceSystem.title": "Höhenbezugssystem",
    "ui.obj.baw.measuring.heightReferenceSystem.help": "",
    "ui.obj.baw.measuring.measuringFrequency.title": "Frequenz der Messung [s]",
    "ui.obj.baw.measuring.measuringFrequency.help": "",
    "ui.obj.baw.timezone.title": "Zeitzone",
    "ui.obj.baw.timezone.help": "",
    "ui.obj.baw.measuring.averageWaterLevel.title": "Gemittelter Wasserstand, auf den sich die Messwerte beziehen",
    "ui.obj.baw.measuring.averageWaterLevel.help": "",
    "ui.obj.baw.measuring.averageWaterLevel.waterLevel": "Wasserstand",
    "ui.obj.baw.measuring.averageWaterLevel.unitOfMeasurement": "Maßeinheit",
    "ui.obj.baw.measuring.zeroLevel.title": "Pegelnullpunkt",
    "ui.obj.baw.measuring.zeroLevel.help": "",
    "ui.obj.baw.measuring.zeroLevel.zeroLevel": "Pegelnullpunkt",
    "ui.obj.baw.measuring.zeroLevel.unitOfMeasurement": "Maßeinheit",
    "ui.obj.baw.measuring.zeroLevel.verticalCoordinateReferenceSystem": "Höhenbezugsszstem",
    "ui.obj.baw.measuring.zeroLevel.description": "Beschreibung",
    "ui.obj.baw.measuring.drainMin.title": "Abfluss Q min [m³/s]",
    "ui.obj.baw.measuring.drainMin.help": "",
    "ui.obj.baw.measuring.drainMax.title": "Abfluss Q max [m³/s]",
    "ui.obj.baw.measuring.drainMax.help": "",
    "ui.obj.baw.measuring.gauge.title": "Messgerät",
    "ui.obj.baw.measuring.gauge.help": "",
    "ui.obj.baw.measuring.gauge.name": "Gerätename",
    "ui.obj.baw.measuring.gauge.id": "Gerät-ID",
    "ui.obj.baw.measuring.gauge.model": "Gerätemodell",
    "ui.obj.baw.measuring.gauge.description": "Beschreibung",
    "ui.obj.baw.measuring.targetParameters.title": "Zielparameter/-Größen",
    "ui.obj.baw.measuring.targetParameters.help": "",
    "ui.obj.baw.measuring.targetParameters.name": "Name",
    "ui.obj.baw.measuring.targetParameters.type": "Art",
    "ui.obj.baw.measuring.targetParameters.unitOfMeasurement": "Maßeinheit",
    "ui.obj.baw.measuring.targetParameters.formula": "Formel/Funktion",
    "ui.obj.baw.measuring.dataQualityDescription.title": "Beschreibung der Datenqualität",
    "ui.obj.baw.measuring.dataQualityDescription.help": "",
    "ui.obj.baw.software.purpose.title": "Einsatzzweck",
    "ui.obj.baw.software.purpose.help": "",
    "ui.obj.baw.software.usergroup.title": "Nutzerkreis",
    "ui.obj.baw.software.usergroup.help": "",
    "ui.obj.baw.software.usergroup.baw": "BAW",
    "ui.obj.baw.software.usergroup.wsv": "WSV",
    "ui.obj.baw.software.usergroup.extern": "Extern",
    "ui.obj.baw.software.usergroup.notes.title": "Ergänzungen und Erläuterungen",
    "ui.obj.baw.software.usergroup.notes.help": "",
    "ui.obj.baw.software.productiveUse.title": "Produktiver Einsatz",
    "ui.obj.baw.software.productiveUse.help": "",
    "ui.obj.baw.software.productiveUse.wsvContract": "WSV-Auftrag",
    "ui.obj.baw.software.productiveUse.fue": "FuE",
    "ui.obj.baw.software.productiveUse.other": "Andere",
    "ui.obj.baw.software.productiveUse.notes.title": "Ergänzungen und Erläuterungen",
    "ui.obj.baw.software.productiveUse.notes.help": "",
    "ui.obj.baw.software.supplementaryModule.label": "Ergänzungsmodul zu Marktsoftware",
    "ui.obj.baw.software.supplementaryModule.yes": "Ja",
    "ui.obj.baw.software.supplementaryModule.no": "Nein",
    "ui.obj.baw.software.name.title": "Name der Marktsoftware",
    "ui.obj.baw.software.name.help": "",
    "ui.obj.baw.software.operatingSystem.title": "Betriebssystem",
    "ui.obj.baw.software.operatingSystem.help": "",
    "ui.obj.baw.software.operatingSystem.windows": "Windows",
    "ui.obj.baw.software.operatingSystem.linux": "Linux",
    "ui.obj.baw.software.operatingSystem.notes": "Ergänzungen und Erläuterungen",
    "ui.obj.baw.software.programmingLanguage.title": "Programmiersprache",
    "ui.obj.baw.software.programmingLanguage.help": "",
    "ui.obj.baw.software.developmentEnvironment.title": "Entwicklungsumgebung",
    "ui.obj.baw.software.developmentEnvironment.help": "",
    "ui.obj.baw.software.libraries.title": "Bibliotheken",
    "ui.obj.baw.software.libraries.help": "",
    "ui.obj.baw.software.libraries.library": "Bibliothek",
    "ui.obj.baw.software.creationContract.title": "Erstellungsvertrag",
    "ui.obj.baw.software.creationContract.help": "",
    "ui.obj.baw.software.creationContract.number.title": "Vertragsnr.",
    "ui.obj.baw.software.creationContract.number.help": "",
    "ui.obj.baw.software.creationContract.date.title": "Datum",
    "ui.obj.baw.software.creationContract.date.help": "",
    "ui.obj.baw.software.supportContract.title": "Supportvertrag",
    "ui.obj.baw.software.supportContract.help": "",
    "ui.obj.baw.software.supportContract.number.title": "Vertragsnr.",
    "ui.obj.baw.software.supportContract.number.help": "",
    "ui.obj.baw.software.supportContract.date.title": "Datum",
    "ui.obj.baw.software.supportContract.date.help": "",
    "ui.obj.baw.software.supportContract.notes.title": "Anmerkungen",
    "ui.obj.baw.software.supportContract.notes.help": "",
    "ui.obj.baw.software.installation.title": "Installationsort",
    "ui.obj.baw.software.installation.help": "",
    "ui.obj.baw.software.installation.local": "Lokal",
    "ui.obj.baw.software.installation.hpc": "HPC",
    "ui.obj.baw.software.installation.server": "Server",
    "ui.obj.baw.software.installBy.title": "Installation über",
    "ui.obj.baw.software.installBy.help": "",
    "ui.obj.baw.software.sourceRightsNotes.title": "Anmerkungen",
    "ui.obj.baw.software.sourceRightsNotes.help": "",
    "ui.obj.baw.software.sourceRights.label": "BAW-Rechte an den Quellen",
    "ui.obj.baw.software.sourceRights.yes": "Ja",
    "ui.obj.baw.software.sourceRights.no": "Nein",
    "ui.obj.baw.software.usageRightsNotes.title": "Anmerkungen",
    "ui.obj.baw.software.usageRightsNotes.help": "",
    "ui.obj.baw.software.usageRights.label": "Rechte bei Nutzung der SW durch Dritte",
    "ui.obj.baw.software.usageRights.yes": "Ja",
    "ui.obj.baw.software.usageRights.no": "Nein",

    "ui.obj.baw.literature.handle.title": "Persistenter Handle",
    "ui.obj.baw.literature.handle.help": "Persistenter Handle für die Publikation als URL z.B. https://hdl.handle.net/20.500.11970/000000",
    "ui.obj.baw.literature.publisher.title": "Herausgeber (ohne Adressbuch-Eintrag)",
    "ui.obj.baw.literature.publisher.help": "Angabe des Herausgebers",
    "ui.obj.baw.literature.author.table.column.family.name": "Nachname",
    "ui.obj.baw.literature.author.table.column.given.name": "Vorname",
    "ui.obj.baw.literature.author.table.column.organisation": "Organisation",
    "ui.obj.baw.literature.author.table.column.author.is.organisation": "Autor ist Organisation",
    "ui.obj.baw.literature.table.row.identifier": "Identifikator",
    "ui.obj.baw.literature.author.table.title": "Autoren (ohne Adressbuch-Eintrag)",
    "ui.obj.baw.literature.author.table.help": "Liste der Autoren, die nicht als Kontakte angelegt sind. Autoren können entweder Personen oder Organisationen sein. Für Personen müssen sowohl Vor- als auch Nachname angegeben werden. Für Organisationen können Vor- und Nachname leer gelassen werden.",

    "ui.sysList.3950000": "BAW - Räumliche Dimensionalität",
    "ui.sysList.3950001": "BAW - Simulationsverfahren",
    "ui.sysList.3950002": "BAW - Bezeichnung der Hierarchieebene",
    "ui.sysList.3950003": "BAW - Modellart",
    "ui.sysList.3950004": "BAW - Simulationsparameter/-Größe Rolle",
    "ui.sysList.3950005": "BAW - Schlagwortkatalog 2012",
    "ui.sysList.3950006": "BAW - Zusätzliche Höhenreferenzsysteme",
    "ui.sysList.3950010": "BAW - VV-WSV 1103",
    "ui.sysList.3950011": "BAW - Messverfahren",
    "ui.sysList.3950012": "BAW - Räumlichkeit der Messdaten",
    "ui.sysList.3950013": "BAW - Messgerätenamen",
    "ui.sysList.3950014": "BAW - Messparameterart",
    "ui.sysList.3950020": "BAW - Maßeinheit",
    "ui.sysList.3950021": "BAW - Physikalische Parameter und Zustandsgrößen",
    "ui.sysList.3950031": "BAW - Software-Entwicklungsumgebung",
    "ui.sysList.3950032": "BAW - Software-Installationsmethode",
    "ui.sysList.3950031": "BAW - HPC",

    "validation.baw.address.role.pointOfContact": "Ein Eintrag für die Institution 'Bundesanstalt für Wasserbau' als 'Ansprechpartner' muss vorhanden sein.",
    "validation.baw.address.software": "Es müssen mindestens 'Verfahrensbetreuung', 'Entwickler' und 'Eigentümer' eingetragen sein.",
    "validation.baw.bwastr_km.entry.missing": "Km-von und Km-bis müssen entweder beide definiert sein oder sollen beide fehlen.",
    "validation.baw.bwastr_name.missing": "Für jeden Streckenabschnitt muss mindestens den Streckenname angegeben werden.",
    "validation.baw.literature.authors.count": "Mindestens ein Autor muss definiert sein. Autoren können entweder in der Adressen- oder in der Autoren-Tabelle angegeben werden.",
    "validation.baw.literature.authors.names": "Für Personen müssen sowohl Vor- als auch Nachname angegeben werden.",
    "validation.baw.literature.publication.date.count": "Das Veröffentlichungsdatum ist eine Pflichtangabe. Es muss genau ein Veröffentlichungsdatum angegeben werden.",
    "validation.baw.literature.publishers": "Es muss im Metadatensatz genau einen Herausgeber angegeben werden. Herausgeber kann entweder in der Adressen-Tabelle oder im Herausgeber-Textfeld angegeben werden.",
    "validation.baw.literature.authors": "Für Personen müssen sowohl Vor- als auch Nachname angegeben werden.",
    "validation.baw.averageWaterLevel.incomplete": "Die Spalten 'Wasserstand' und 'Maßeinheit' sind verpflichtend.",
    "validation.baw.zeroLevel.incomplete": "Die Spalten 'Pegelnullpunkt', 'Maßeinheit' und 'Vertikales Koordinatenreferenzsystem' sind verpflichtend.",
    "validation.baw.gauge.incomplete": "Die Spalte 'Gerätename' ist verpflichtend.",
    "validation.baw.targetParameters.incomplete": "Die Spalten 'Name', 'Art' und 'Maßeinheit' sind verpflichtend.",
    "validation.baw.supplementaryModule.required": "Bitte wählen Sie eine Option: Ja/Nein",

    "error.invalid.psp.number": "Die Auftragsnummer ist ungültig.",
    "error.no.bwastrid.specified": "Die BWaStr.-ID aus den Streckenabschnitten ist ungültig.",
    "error.moving.object": "Es trat ein Fehler beim Verschieben der Datei/des Ordners auf"
});

