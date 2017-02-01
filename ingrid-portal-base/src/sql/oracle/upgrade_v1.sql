---
-- **************************************************-
-- Ingrid Portal Base
-- ==================================================
-- Copyright (C) 2014 wemove digital solutions GmbH
-- ==================================================
-- Licensed under the EUPL, Version 1.1 or – as soon they will be
-- approved by the European Commission - subsequent versions of the
-- EUPL (the "Licence");
-- 
-- You may not use this work except in compliance with the Licence.
-- You may obtain a copy of the Licence at:
-- 
-- http://ec.europa.eu/idabc/eupl5
-- 
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the Licence is distributed on an "AS IS" basis,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the Licence for the specific language governing permissions and
-- limitations under the Licence.
-- **************************************************#
---
INSERT INTO ingrid_lookup (id, item_key, item_value) VALUES (1, 'ingrid_db_version', '1');


INSERT INTO ingrid_cms (id, item_key, item_description, item_changed, item_changed_by) VALUES (1, 'portalu.teaser.inform', 'PortalU informiert Text', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms (id, item_key, item_description, item_changed, item_changed_by) VALUES (15, 'portalu.disclaimer', 'Impressum', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms (id, item_key, item_description, item_changed, item_changed_by) VALUES (16, 'portalu.about', 'Über PortalU', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms (id, item_key, item_description, item_changed, item_changed_by) VALUES (17, 'portalu.privacy', 'Haftungsausschluss', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms (id, item_key, item_description, item_changed, item_changed_by) VALUES (18, 'portalu.contact.intro.postEmail', 'Adresse auf der Kontaktseite', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms (id, item_key, item_description, item_changed, item_changed_by) VALUES (19, 'ingrid.home.welcome', 'Ingrid Willkommens Portlet', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');


INSERT INTO ingrid_cms_item (id, fk_ingrid_cms_id, item_lang, item_title, item_value, item_changed, item_changed_by) VALUES (1, 1, 'de', '<span style="text-transform: none;">PortalU INFORMIERT</span>', '<p>Besuchen Sie PortalU® vom 6. bis 8. September auf dem 20. Internationalen Symposium Umweltinformatik - EnviroInfo 2006 - in Graz</p> <p class="iconLink"> <span class="icon"><img src="/ingrid-portal-apps/images/icn_linkextern.gif" alt="externer Link"></span> <span><a href="http://enviroinfo.know-center.tugraz.at/" target="_new" title="Link öffnet in neuem Fenster">enviroinfo.know-center.tugraz.at/</a></span> <span class="clearer"></span></p>', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms_item (id, fk_ingrid_cms_id, item_lang, item_title, item_value, item_changed, item_changed_by) VALUES (2, 1, 'en', '<span style="text-transform: none;">PortalU INFORMS</span>', '<p>Visit PortalU® on September 6 to 8 at the 20th International Symposium on Informatics for Environmental Protection - EnviroInfo 2006 - in Graz (Austria)</p> <p class="iconLink"> <span class="icon"><img src="/ingrid-portal-apps/images/icn_linkextern.gif" alt="External Link"></span> <span><a href="http://enviroinfo.know-center.tugraz.at/" target="_new" title="Link opens new window">enviroinfo.know-center.tugraz.at/</a></span> <span class="clearer"></span></p>', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms_item (id, fk_ingrid_cms_id, item_lang, item_title, item_value, item_changed, item_changed_by) VALUES (27, 15, 'en', 'Disclaimer', '<a name="herausgeber"></a>\r\n<h2>Publisher</h2>\r\n<p>PortalU is managed by the Coordination Center PortalU at the Environment Ministry of Lower Saxony, Hannover, Germany. Development and maintenance of the portal is financed by a administrative agreement between the German Federal States (Länder) and the Federal Government. </p>\r\n<h3>Coordination Center PortalU</h3>\r\n<p>Niedersächsisches Umweltministerium<br>Archivstrasse 2<br>D-30169 Hannover<br>\r\n	<a href="/ingrid-portal/portal/service-contact.psml">Contact</a>\r\n</p>\r\n<br>\r\n<a name="verantwortlich"></a>\r\n<h2>Overall Responsibility</h2>\r\n<p>Dr. Fred Kruse</p>\r\n<br>\r\n<a name="realisierung"></a>\r\n<h2>Implementation</h2>\r\n<h3><a href="http://www.gistec-online.de/" target="_new" title="Link opens new window">GIStec GmbH</a></h3>\r\n<h3><a href="http://www.media-style.com/" target="_new" title="Link opens new window">media style GmbH</a></h3>\r\n<h3><a href="http://www.wemove.com/contact.php" target="_new" title="Link opens new window">wemove digital solutions GmbH</a></h3>\r\n<h3><a href="http://www.chives.de/" target="_new" title="Link opens new window">chives - Büro für Webdesign Plus</a></h3>\r\n<br>\r\n<a name="betrieb"></a>\r\n<h2>Operation</h2>     \r\n<h3><a href="http://www.its-technidata.de/" target="_new" title="Link opens new window">TechniData IT Service GmbH</a></h3>\r\n<br>\r\n<a name="haftung"></a>\r\n<h2>Liability Disclaimer</h2>     \r\n<p>The Environment Ministry of Lower Saxony (Niedersächsisches Umweltministerium) does not take any responisbility for the content of web-sites that can be reached through PortalU. Web-sites that are included in the portal are evaluated only technically. A continuous evaluation of the content of the included web-pages in neither possible nor intended. The Environment Ministry of Lower Saxony explicitly rejects all content that potentially infringes upon German legislation or general morality.</p>', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms_item (id, fk_ingrid_cms_id, item_lang, item_title, item_value, item_changed, item_changed_by) VALUES (28, 15, 'de', 'Impressum', '<a name="herausgeber"></a>\r\n<h2>Herausgeber</h2>\r\n<p>PortalU wird von der Koordinierungsstelle PortalU im Niedersächsischen Umweltministerium auf der Grundlage der <a href="http://www.kst.portalu.de/ueberuns/VVGEIN_endg.pdf" target="_new" title="Link öffnet in neuem Fenster">Bund-Länder-Verwaltungsvereinbarung UDK/GEIN</a> betrieben und weiterentwickelt.</p>\r\n<h3>Koordinierungsstelle PortalU</h3>\r\n<p>\r\nNiedersächsisches Umweltministerium<br>Archivstrasse 2<br>D-30169 Hannover<br>\r\n<a href="/ingrid-portal/portal/service-contact.psml">Kontakt</a>\r\n</p>\r\n<br>\r\n<a name="verantwortlich"></a>\r\n<h2>Verantwortliche Gesamtredaktion</h2>\r\n<p>Dr. Fred Kruse</p>\r\n<br>\r\n<a name="realisierung"></a>\r\n<h2>Realisierung</h2>\r\n<h3><a href="http://www.gistec-online.de/" target="_new" title="Link öffnet in neuem Fenster">GIStec GmbH</a></h3>\r\n<h3><a href="http://www.media-style.com/" target="_new" title="Link öffnet in neuem Fenster">media style GmbH</a></h3>\r\n<h3><a href="http://www.wemove.com/contact.php" target="_new" title="Link öffnet in neuem Fenster">wemove digital solutions GmbH</a></h3>\r\n<h3><a href="http://www.chives.de/" target="_new" title="Link öffnet in neuem Fenster">chives - Büro für Webdesign Plus Darmstadt</a></h3>\r\n<br>\r\n<a name="betrieb"></a>\r\n<h2>Technischer Betrieb</h2>     \r\n<h3><a href="http://www.its-technidata.de/" target="_new" title="Link öffnet in neuem Fenster">TechniData IT Service GmbH</a></h3>\r\n<br>\r\n<a name="haftung"></a>\r\n<h2>Haftungsausschluss</h2>     \r\n<p>Das Niedersächsische Umweltministerium übernimmt keine Verantwortung für die Inhalte von Websites, die über Links erreicht werden. Die Links werden bei der Aufnahme nur kursorisch angesehen und bewertet. Eine kontinuierliche Prüfung der Inhalte ist weder beabsichtigt noch möglich. Das Niedersächsische Umweltministerium distanziert sich ausdrücklich von allen Inhalten, die möglicherweise straf- oder haftungsrechtlich relevant sind oder gegen die guten Sitten verstoßen.</p>', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms_item (id, fk_ingrid_cms_id, item_lang, item_title, item_value, item_changed, item_changed_by) VALUES (29, 16, 'de', 'Über PortalU', '<p>Willkommen bei PortalU, dem Umweltportal Deutschland! Wir bieten Ihnen einen zentralen Zugriff auf mehrere hunderttausend Internetseiten und Datenbankeinträge von öffentlichen Institutionen und Organisationen. Zusätzlich können Sie aktuelle Nachrichten und Veranstaltungshinweise, Umweltmesswerte, Hintergrundinformationen und historische Umweltereignisse über PortalU abrufen.</p><p>Die integrierte Suchmaschine ist eine zentrale Komponente von PortalU. Mit ihrer Hilfe können Sie Webseiten und Datenbankeinträge nach Stichworten durchsuchen. Über die Option "Erweiterte Suche" können Sie zusätzlich ein differenziertes Fachvokabular und deutschlandweite Hintergrundkarten zur Zusammenstellung Ihrer Suchanfrage nutzen.</p><p>PortalU ist eine Kooperation der Umweltverwaltungen im Bund und in den Ländern. Inhaltlich und technisch wird PortalU von der <a href="http://www.kst.portalu.de/" target="_new" title="Link öffnet in neuem Fenster">Koordinierungsstelle PortalU</a> im Niedersächsischen Umweltministerium verwaltet. Wir sind darum bemüht, das System kontinuierlich zu erweitern und zu optimieren. Bei Fragen und Verbesserungsvorschlägen wenden Sie sich bitte an die <a href="mailto:kst@portalu.de">Koordinierungsstelle PortalU</a>.</p>', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms_item (id, fk_ingrid_cms_id, item_lang, item_title, item_value, item_changed, item_changed_by) VALUES (30, 16, 'en', 'About PortalU', '<p>Welcome to PortalU, the German Environmental Information Portal! We offer a comfortable and central access to over 1.000.000 web-pages and database entries from public agencies in Germany. We also guide you directly to up-to-date environmental news, upcoming and past environmental events, environmental monitoring data, and interesting background information on many environmental topics.</p><p>Core-component of PortalU is a powerful search-engine that you can use to look up your terms of interest in web-pages and databases. In the "Extended Search" mode, you can use an environmental thesaurus and a digital mapping tool to compose complex spatio-thematic queries.</p><p>PortalU is the result of a cooperation between the German "Länder" and the German Federal Government. The project is managed by the <a href="http://www.kst.portalu.de/" target="_new" title="Link opens new window">Coordination Center PortalU</a>, a group of environmental and IT experts attached to the Environment Ministry of Lower Saxony in Hannover, Germany. We strive to continuously improve and extend the portal. Please help us in this effort and mail your suggestions or questions to <a href="mailto:kst@portalu.de">Coordination Center PortalU</a>.</p>', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms_item (id, fk_ingrid_cms_id, item_lang, item_title, item_value, item_changed, item_changed_by) VALUES (31, 17, 'de', 'Datenschutz', '<p>PortalU enthält sowohl Inhalte, die als Teledienst nach § 2 Teledienstgesetz (TDG) als auch Inhalte, die als Mediendienst nach § 2 Mediendienste-Staatsvertrag (MDStV) zu bewerten sind. Hierbei werden folgende Verfahrensgrundsätze gewährleistet:<br></p>\r\n<ul>\r\n<li>Bei jedem Zugriff eines Nutzers auf eine Seite aus dem Angebot von PortalU und bei jedem Abruf einer Datei werden Daten über diesen Vorgang in einer Protokolldatei gespeichert. Diese Daten sind nicht personenbezogen. Wir können also nicht nachvollziehen, welcher Nutzer welche Daten abgerufen hat. Die Protokolldaten werden lediglich in anonymisierter Form statistisch ausgewertet und dienen damit der inhaltlichen Verbesserung unseres Angebotes.<br><br>\r\n</li>\r\n<li>Eine Ausnahme besteht innerhalb des Internetangebotes mit der Eingabe persönlicher oder geschäftlicher Daten (eMail-Adresse, Name, Anschrift) zur Anmeldung bei "Mein PortalU" oder der Bestellung des PortalU-Newsletters. Dabei erfolgt die Angabe dieser Daten durch Nutzerinnen und Nutzer ausdrücklich freiwillig. Ihre persönlichen Daten werden von uns selbstverständlich nicht an Dritte weitergegeben. Die Inanspruchnahme aller angebotenen Dienste ist, soweit dies technisch möglich und zumutbar ist, auch ohne Angabe solcher Daten beziehungsweise unter Angabe anonymisierter Daten oder eines Pseudonyms möglich.<br><br>\r\n</li>\r\n<li>Sie können alle allgemein zugänglichen PortalU-Seiten ohne den Einsatz von Cookies benutzen. Wenn Ihre Browser-Einstellungen das Setzen von Cookies zulassen, werden von PortalU sowohl Session-Cookies als auch permanente Cookies gesetzt. Diese dienen ausschließlich der Erhöhung des Bedienungskomforts.\r\n</li>\r\n</ul>', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms_item (id, fk_ingrid_cms_id, item_lang, item_title, item_value, item_changed, item_changed_by) VALUES (32, 17, 'en', 'Privacy Policy', '<p>PortalU contains content that is categorized as "Teledienst" (after § 2 Teledienstgesetz (TDG)), as well as content that is categorized as "Mediendienst" (after § 2 Mediendienste-Staatsvertrag (MDStV)). The following policies do apply:<br></p>\r\n<ul>\r\n<li>With each user-access to a content-page in PortalU, the relevant access-data are saved in a log file. This information is not personalized. Therefore it is not possible to reason which user has had access to which content page. The purpose of the log file is purely statistical. The evaluation of the log file helps to improve PortalU.<br><br>\r\n</li>\r\n<li>An exeption to our general privacy policy is made when personal data (e-mail, name, address) are provided to register for the PortalU newsletter. This information is provided by the user on a voluntary basis an is saved for internal purposes (mailing of newsletter). The information is not given to third-parties. The use of specific Portal functions does not, as far as technically feasible, depend on the provision of personal data.<br><br>\r\n</li>\r\n<li>You can take benefit of virtually all functions of PortalU without the use of cookies. However, if you choose to allow cookies in your browser, this will increase the useability of the application. \r\n</li>\r\n</ul>', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms_item (id, fk_ingrid_cms_id, item_lang, item_title, item_value, item_changed, item_changed_by) VALUES (33, 18, 'de', '', '.</p><p>Unsere Postadresse:</p>\r\n<p>Niedersächsisches Umweltministerium<br />Koordinierungsstelle PortalU<br />Archivstrasse 2<br />D-30169 Hannover<br /></p> <p>Nehmen Sie online Kontakt mit uns auf! Wir werden Ihnen schnellstmöglichst per E-Mail antworten. Die eingegebenen Informationen und Daten werden nur zur Bearbeitung Ihrer Anfrage gespeichert und genutzt.</p>', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms_item (id, fk_ingrid_cms_id, item_lang, item_title, item_value, item_changed, item_changed_by) VALUES (34, 18, 'en', '', '.</p><p>Our address:</p><p>Niedersächsisches Umweltministerium<br />Koordinierungsstelle PortalU<br />Archivstrasse 2<br />D-30169 Hannover<br /></p> <p>Please contact us! We will answer your request as soon as possible. All data you entered will be saved only to process your request.</p>', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms_item (id, fk_ingrid_cms_id, item_lang, item_title, item_value, item_changed, item_changed_by) VALUES (35, 19, 'de', 'Willkommen bei PortalU', '<p>Willkommen bei PortalU, dem Umweltportal Deutschland! Wir bieten Ihnen einen zentralen Zugriff auf mehrere hunderttausend Internetseiten und Datenbankeinträge von öffentlichen Institutionen und Organisationen. Zusätzlich können Sie aktuelle Nachrichten und Veranstaltungshinweise, Umweltmesswerte, Hintergrundinformationen und historische Umweltereignisse über PortalU abrufen.</p><p>Die integrierte Suchmaschine ist eine zentrale Komponente von PortalU. Mit ihrer Hilfe können Sie Webseiten und Datenbankeinträge nach Stichworten durchsuchen. Über die Option "Erweiterte Suche" können Sie zusätzlich ein differenziertes Fachvokabular und deutschlandweite Hintergrundkarten zur Zusammenstellung Ihrer Suchanfrage nutzen.</p><p>PortalU ist eine Kooperation der Umweltverwaltungen im Bund und in den Ländern. Inhaltlich und technisch wird PortalU von der <a href="http://www.kst.portalu.de/" target="_new" title="Link öffnet in neuem Fenster">Koordinierungsstelle PortalU</a> im Niedersächsischen Umweltministerium verwaltet. Wir sind darum bemüht, das System kontinuierlich zu erweitern und zu optimieren. Bei Fragen und Verbesserungsvorschlägen wenden Sie sich bitte an die <a href="mailto:kst@portalu.de">Koordinierungsstelle PortalU</a>.</p>', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');
INSERT INTO ingrid_cms_item (id, fk_ingrid_cms_id, item_lang, item_title, item_value, item_changed, item_changed_by) VALUES (36, 19, 'en', 'Welcome to PortalU', '<p>Welcome to PortalU, the German Environmental Information Portal! We offer a comfortable and central access to over 1.000.000 web-pages and database entries from public agencies in Germany. We also guide you directly to up-to-date environmental news, upcoming and past environmental events, environmental monitoring data, and interesting background information on many environmental topics.</p><p>Core-component of PortalU is a powerful search-engine that you can use to look up your terms of interest in web-pages and databases. In the "Extended Search" mode, you can use an environmental thesaurus and a digital mapping tool to compose complex spatio-thematic queries.</p><p>PortalU is the result of a cooperation between the German "Länder" and the German Federal Government. The project is managed by the <a href="http://www.kst.portalu.de/" target="_new" title="Link opens new window">Coordination Center PortalU</a>, a group of environmental and IT experts attached to the Environment Ministry of Lower Saxony in Hannover, Germany. We strive to continuously improve and extend the portal. Please help us in this effort and mail your suggestions or questions to <a href="mailto:kst@portalu.de">Coordination Center PortalU</a>.</p>', to_date('2006-09-15 00:00:00','YYYY-MM-DD HH24:MI:SS'), 'admin');

UPDATE ingrid_provider SET name = 'Gewerbeaufsicht Baden-Württemberg' WHERE id =46;
UPDATE ingrid_rss_source SET url = 'http://www.bfn.de/0502_skripten.100.html' WHERE id =4;
UPDATE ingrid_provider SET name = 'Landesforsten Rheinland-Pfalz' WHERE id =115;
UPDATE ingrid_provider SET name = 'Ministerium für Umwelt, Forsten und Verbraucherschutz Rheinland-Pfalz' WHERE id =112;
INSERT INTO ingrid_provider (id, ident, name, url, sortkey, sortkey_partner) VALUES (137, 'rp_luwg', 'Landesamt für Umwelt, Wasserwirtschaft und Gewerbeaufsicht Rheinland-Pfalz', 'http://www.luwg.rlp.de/', 4, 12);
INSERT INTO ingrid_provider (id, ident, name, url, sortkey, sortkey_partner) VALUES (138, 'rp_lua', 'Landesuntersuchungsamt Rheinland-Pfalz', 'http://www.lua.rlp.de/', 5, 12);
INSERT INTO ingrid_provider ( id , ident , name , url , sortkey , sortkey_partner ) VALUES (139, 'bw_saa', 'SAA Sonderabfallagentur', 'http://www.saa.de/', '12', '2');
INSERT INTO ingrid_provider ( id , ident , name , url , sortkey , sortkey_partner ) VALUES (140, 'bw_rp', 'Regierungspräsidien Baden-Württemberg', 'http://www.rp.baden-wuerttemberg.de/', '13', '2');
INSERT INTO ingrid_provider ( id , ident , name , url , sortkey , sortkey_partner ) VALUES (141, 'bw_rps', 'Regierungspräsidium Stuttgart', 'http://www.landentwicklung-mlr.baden-wuerttemberg.de/', '14', '2');

DELETE FROM ingrid_provider WHERE id =113;
DELETE FROM ingrid_provider WHERE id =114;

UPDATE ingrid_provider SET sortkey = 1 WHERE id =112;
UPDATE ingrid_provider SET sortkey = 2 WHERE id =115;
UPDATE ingrid_provider SET sortkey = 3 WHERE id =116;
UPDATE ingrid_provider SET sortkey = 4 WHERE id =137;
UPDATE ingrid_provider SET sortkey = 5 WHERE id =138;

UPDATE ingrid_rss_source SET url = 'http://www.mufv.rlp.de/rss/rss_1_20.xml' WHERE id =3;

INSERT INTO ingrid_rss_source (id, provider, description, url, lang, categories) VALUES (12, 'bw_lu', 'Landesanstalt für Umwelt, Messungen und Naturschutz Baden-Württemberg', 'http://www.lubw.baden-wuerttemberg.de/servlet/is/Entry.20732.DisplayRSS2/', 'de', 'all');
INSERT INTO ingrid_rss_source (id, provider, description, url, lang, categories) VALUES (13, 'bw_statistik', 'RSS Badenwürtemberg (Statistik)', 'http://www.statistik.baden-wuerttemberg.de/UmweltVerkehr/rss.aspx', 'de', 'all');
