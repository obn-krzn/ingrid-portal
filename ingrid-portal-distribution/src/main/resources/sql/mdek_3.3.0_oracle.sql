--
-- Daten für Tabelle help_messages
--

UPDATE info SET value_name = '3.3' WHERE  info.key_name = 'version';

-- New stuff for InGrid 3.3
-- ========================

INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1396, 0, 5100, -1, 'de', 'Vorschaugrafik', 'Tragen Sie hier eine URL ein, unter welcher eine Grafik abgerufen werden kann. Diese Grafik wird im Portal in der Suchergebnisliste neben dem Treffer angezeigt.', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1397, 0, 5100, -1, 'en', 'Preview image', 'Enter a URL pointing to an image file on the web that will be used in the portal when presenting the search results.', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1398, 0, 7103, -1, 'de', 'Zusatzinformation', 'Angabe von zusätzlichen Informationen wie zum Beispiel die Veröffentlichungsbreite.', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1399, 0, 7103, -1, 'en', 'Preview image', 'Add additional information as for example the publication condition.', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1400, 0, 4571, -1, 'de', 'Veröffentlichung', 'Das Feld Veröffentlichung gibt an, welche Veröffentlichungsmöglichkeiten für die Adresse freigegeben sind. Die Liste der Möglichkeiten ist nach Freigabestufen hierarchisch geordnet. Wird einer Adresse eine niedrigere Freigabestufe zugeordnet (z.B. von Internet auf Intranet), werden automatisch auch alle untergeordneten Adressen dieser Stufe zugeordnet. Soll einer Adresse eine höhere Freigabestufe zugeordnet werden als die der übergeordneten Adresse, wird die Zuordnung verweigert. Wird einer Adresse eine höhere Freigabestufe zugeordnet (z.B. von Amtsintern auf Intranet), kann auch allen untergeordneten Adressen die höhere Freigabestufe zugeordnet werden. Beim Veröffentlichen wird zusätzlich überprüft, ob die Veröffentlichungsbreite von verweisenden Objekten, mit der der Adresse gültig ist.', 'Die Einstellung haben folgende Bedeutung: Internet: Die Adresse darf überall veröffentlicht werden; Intranet: Die Adresse darf nur im Intranet veröffentlicht werden, aber nicht im Internet; Amtsintern: Die Adresse darf nur in der erfassenden Instanz aber weder im Internet noch im Intranet veröffentlicht werden.');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1401, 0, 4571, -1, 'en', 'Publication condition', 'The publication field details which publication possibilities have been allowed for the address. The list of the possibilities has been ordered hierarchically according to release levels. If a lower release level has been dedicated to an address (e.g. from internet to intranet), all the addresses subordinate to this will be automatically dedicated to this level. If a higher release level is dedicated to an address than the one dedicated to the subordinate address then the action is rejected. If a higher release level is dedicated to an address (e.g. in-house on intranet), then all the subordinate addresses will also be dedicated to the higher release level. During publication it is also checked if the publication condition of linked objects are valid with the one from the address.', 'The setting has the following significance: Internet: the address may be published everywhere; Intranet: the address may only be published in the intranet but not in the internet; In-house: the address may only be published as it is created but neither in the internet nor in the intranet.');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1402, 0, 8208, -1, 'de', 'Exportieren von Arbeitsversionen', 'Diese Option ermöglicht neben den veröffentlichten Objekten und Adressen, auch die sich in Bearbeitung befindlichen Objekte/Adressen zu exportieren. Dies entspricht somit einem vollständigen Backup, welches über den Import wieder hergestellt werden kann.', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1403, 0, 8208, -1, 'en', 'Export of working copies', 'This option allows to also export the working copies of objects and addresses. This export is equal to a full backup which can be restored with the import function of the IGE.', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1404, 0, 8209, -1, 'de', 'Überschreiben von Metadaten', 'In diesem Fall werden alle Metadaten, die mit den Importdaten über die ID übereinstimmen, überschrieben. Darüber hinaus kann sich auch die Position der überschriebenen Metadaten im Hierarchiebaum ändern.', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1405, 0, 8209, -1, 'en', 'Overwrite of Metadata', 'In this case all metadata, which have the same ID as the imported data are overwritten. Moreover the position of the overwritten metadata can change during the import.', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1406, 0, 8210, -1, 'de', 'Verantwortlich für Objekte', 'Diese Tabelle zeigt alle Objekte an, für die der ausgewählter Nutzer als Verantwortlicher eingetragen worden ist.', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1407, 0, 8210, -1, 'en', 'Responsible in objects', 'This table lists all the objects that the selected user is responsible for.', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1408, 0, 8211, -1, 'de', 'Verantwortlich für Adressen', 'Diese Tabelle zeigt alle Adressen an, für die der ausgewählter Nutzer als Verantwortlicher eingetragen worden ist.', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1409, 0, 8211, -1, 'en', 'Responsible in addresses', 'This table lists all addresses that the selected user is responsible for.', '');


DELETE FROM help_messages WHERE gui_id = 1000;
DELETE FROM help_messages WHERE gui_id = 1500;
DELETE FROM help_messages WHERE gui_id = 10008;
DELETE FROM help_messages WHERE gui_id = 10021;
DELETE FROM help_messages WHERE gui_id = 8100;
DELETE FROM help_messages WHERE id = 76;
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1, 0, 1000, -1, 'de', 'Adressen', 'Eintrag von Adressverweise zu Personen oder Institutionen, die weitergehende Informationen zum aktuellen Objekt geben können. Bei Bedarf können diese Verweise verändert werden. In der ersten Spalte wird jeweils die Art des Verweises eingetragen ( Auskunft, Datenhalter, etc.). Über den Link "Adresse hinzufügen" wird der Verweis selbst angelegt. Als Auswahlmöglichkeit stehen alle in der Adressverwaltung des aktuellen Kataloges bereits eingetragenen Personen zur Verfügung. Der Eintrag in der ersten Zeile (Auskunft) ist obligatorisch. Weiterhin ist hier ein Eintrag für Datenverantwortung verpflichtend, wenn ein INSPIRE-Thema ausgewählt wurde. Über das Kontextmenü ist es auch möglich Adressen zu kopieren und in dasselbe oder ein anderes wieder einzufügen.', 'Auskunft / Robbe, Antje Datenhalter / Dr. Seehund, Siegfried');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1001, 0, 1000, -1, 'en', 'Addresses', 'Input of address references to persons or institutions, which can provide further information on the current object. If needed, these references can be amended. In the first column the type of reference is entered respectively (information, data holder etc.). The reference is itself created via the "add address" link. All the persons already entered in the address manager of the current catalogue are available for selection. Making an entry in the first line (information) is obligatory. Via the context menu it is possible to copy an address which can be pasted within the same or another object.', 'Information / Sam Sample Data Holder / Jane Sample');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(23, 0, 1500, -1, 'de', 'Verweis zu', 'Es gibt die Möglichkeit, Verweise von einem Objekt zu einem anderen Objekt oder zu einer WWW-Adresse (URL) zu erstellen. In dieser Tabelle werden alle Verweise zusammenfassend aufgeführt, welche im aktuellen Objekt angelegt wurden. Über dem Link "Verweise anlegen/bearbeiten" öffnet sich ein Dialog, mit dem weitere Einzelheiten zu den Verweisen eingesehen und editiert werden können. Es ist ferner möglich, weitere Verweise über diesen Dialog hinzuzufügen. Über den Link "erben" können alle Verweise des übergeordneten Objektes übernommen werden. Dabei werden nur neue Verweise übernommen!', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1023, 0, 1500, -1, 'en', 'Links To', 'There is the possibility of creating references from one object to another object or to a www-address (URL). In this table all references are listed that were created in the current object as a summary. A dialogue box opens via the link "create/edit references" with which further details on the references can be viewed and edited. Additionally, it is possible to add further references via this dialogue box. The link "inherit" copies all references from the parent object to the currently open one. Only new references are copied!', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(311, 0, 10008, -1, 'de', 'Freier Raumbezug ', 'Informationen über die räumliche Zuordnung des in dem Objekt beschriebenen Datenbestand. Es können frei wählbare Raumbezugs-Koordinaten hinzugefügt werden. Der Wertebereich im WGS ist folgendermaßen definiert: <ul><li>Breite (Latitude): -90 bis 90</li><li>Länge (Longitude): -180 bis 180</li></ul>Über den Link "erben" können alle freien Raumbezüge des übergeordneten Objektes übernommen werden. Dabei werden nur neue Raumbezüge übernommen!', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1311, 0, 10008, -1, 'en', 'Custom Location', 'Information on the spatial allocation of the data base described in the object. Freely selectable spatial coordinates can be added. The range of values is defined in WGS as follows: <ul><li>Latitude: -90 to 90</li><li>Longitude: -180 to 180</li></ul>The link "inherit" copies all spatial coordinates from the parent object to the currently open one. Only new coordinates are copied!', '');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(317, 0, 10021, -1, 'de', 'Identifikator der Datenquelle', '<p>Hier muss ein eindeutiger Name (Identifikator) für die im Datenobjekt beschriebene Datenquelle (z.B. eine Karte) vergeben werden. Der Identifikator soll aus einem Namensraum (=codespace), abgeschlossen mit einem Doppelkreuz, sowie einem Code bestehen. (INSPIRE-Pflichtfeld).</p><p>Wenn der Identifikator keinen Namensraum mit ''#''-Zeichen enthält, so wird dem Identifikator bei der Abgabe der Metadaten derjenige Wert vorangestellt, welcher im Bereich Katalogverwaltung/Katalogeinstellungen unter "Namensraum des Katalogs" eingetragen ist. Hierbei wird das ''#'' automatisch ergänzt.</p><p>Der Identifikator kann auch von Hand eingetragen werden oder mit Hilfe des Buttons "Erzeuge ID". Bei der automatischen Erzeugung wird eine UUID als Identifikator in dieses Feld eingetragen. Da diese UUID keinen Namespace enthält, wird bei dieser Variante immer der Namensraum aus der Katalogverwaltung hinzugefügt.</p>', 'Namensraum: http://image2000.jrc.it#<br/>Code: image2000_1_nl2_multi<br/>Identifikator: http://image2000.jrc.it#image2000_1_nl2_multi');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1317, 0, 10021, -1, 'en', 'Identification', '<p>Here a unique identifier has to be given for the data source described by the data object (e.g. a map). The identifier has to consist of a namespace (=codespace), followed by a hashmark, and a code value. (INSPIRE-obligatory field)</p><p>In case that the identifier does not contain a namespace (followed by a ''#''), it will be completed automatically during export (e.g. CSW) with the ''Catalogue Namespace''. This catalogue namespace is specified with a configuration option that can be found in Catalogue/Catalogue Settings. A separating hashmark will be filled in between namespace and identifier.</p><p>The identifier can be filled in by hand or using the button "Create ID". The latter generates a UUID that is inserted into this form element. As the UUID does not contain a namespace it will always be completed with the namespace as specified in the catalogue settings.</p>', 'Namenspace: http://image2000.jrc.it#<br/>Code: image2000_1_nl2_multi<br/>Identifier: http://image2000.jrc.it#image2000_1_nl2_multi');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1374, 0, 8100, -1, 'de', 'Namensraum des Katalogs', '<p>Der Namensraum des Katalogs, der bei der Abgabe der Daten verwendet wird (CSW, OpenSearch).</p><p>Mit diesem Namensraum wird der Inhalt des Feldes ''Identifikator der Datenquelle'' (im Fachbezug der Klasse Geoinformation/Karte) automatisch ergänzt, falls im Identifikator noch kein Namensraum enthalten ist. Die Struktur des damit erzeugten Identifikators ist dann folgende: namensraum#identifikator. Wichtig ist dabei das ''#'' als Trennzeichen zwischen Namensraum und Identifikator.</p>', 'Namensraum des Katalogs: http://portalu.de/igc_test<br/>Identifikator der Datenquelle: 627c05e8-ace4-4ca6-b531-ded7b3c79ee6<br/><br/><p>Als Identifikator der Datenquelle (MD_Identifier) wird dann folgender Wert abgegeben:</p>http://portalu.de/igc_test#627c05e8-ace4-4ca6-b531-ded7b3c79ee6');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(1375, 0, 8100, -1, 'en', 'Catalogue Namespace', '<p>The namespace of the catalogue that is used when exporting data (CSW, Open Search).</p><p>This namespace is used to automatically complete the content of the element "Identification" (section Relevance in class Geo-Information/Map) in case that the identifier does not already contain a namespace. Hereby an identifier of the following structure is created: namespace#identifier. Note that the ''#'' is used as a separator between namespace and identifier.</p>', 'Namespace of the Catalogue: http://portalu.de/igc_test<br/>Identifier of the resource: 627c05e8-ace4-4ca6-b531-ded7b3c79ee6<br/><br/><p>The complete identifier of the resource (MD_Identifier) would be the following:</p>http://portalu.de/igc_test#627c05e8-ace4-4ca6-b531-ded7b3c79ee6');
INSERT INTO help_messages (id, version, gui_id, entity_class, language, name, help_text, sample) VALUES
(76, 0, 3565, 1, 'de', 'Datendefizit', 'Eingabe einer Prozentangabe zum Anteil der Daten, die im Vergleich zum beschriebenen Geltungsbereich fehlen. Diese kann sich auf die Anzahl der Kartenblätter aber auch auf das Datendefizit einer Gesamtkarte beziehen.', '55');