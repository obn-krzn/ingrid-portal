-- Create temporary table
DROP TABLE IF EXISTS ingrid_temp;
CREATE TABLE ingrid_temp (
  temp_key varchar(255) NOT NULL,
  temp_value mediumint
);

-- Hide '/main-measures.psml'
UPDATE page SET is_hidden = 1 WHERE path = '/main-measures.psml';

-- Hide '/main-maps.psml'
UPDATE page SET is_hidden = 1 WHERE path = '/main-maps.psml';

-- Display '/help.psml'
UPDATE page SET is_hidden = 0 WHERE path = '/help.psml';

-- Hide '/accessibility.psml'
UPDATE page SET is_hidden = 0 WHERE path = '/accessibility.psml';

-- Hide '/service-contact.psml'
UPDATE page SET is_hidden = 1 WHERE path = '/service-contact.psml';

-- Hide '/service-sitemap.psml'
UPDATE page SET is_hidden = 1 WHERE path = '/service-sitemap.psml';

-- Hide '/search-catalog/search-catalog-hierarchy.psml'
UPDATE page SET is_hidden = 1 WHERE path = '/search-catalog/search-catalog-hierarchy.psml';

-- Hide '/main-about-data-source.psml'
UPDATE page SET is_hidden = 1 WHERE path = '/main-about-data-source.psml';

-- Hide '/main-about-partner.psml'
UPDATE page SET is_hidden = 1 WHERE path = '/main-about-partner.psml';

-- Hide '/application/main-application.psml'
UPDATE page SET is_hidden = 1 WHERE path = '/application/main-application.psml';

-- Hide folder "/application" to display
UPDATE folder SET is_hidden = 1 WHERE path = '/application';

-- Hide '/administration/admin-statistics.psml'
UPDATE page SET is_hidden = 1 WHERE path = '/administration/admin-statistics.psml';

-- Change '/default-page.psml'
INSERT INTO ingrid_temp (temp_key, temp_value) VALUES ('default_page_fragment_id',(SELECT fragment_id FROM fragment WHERE page_id = (SELECT page_id FROM page WHERE path = '/default-page.psml')));
DELETE FROM fragment WHERE parent_id = (SELECT temp_value FROM ingrid_temp WHERE temp_key = 'default_page_fragment_id');
INSERT INTO fragment (fragment_id, class_name, parent_id, name, type, layout_row, layout_column, layout_x, layout_y, layout_z, layout_width, layout_height) VALUES ((SELECT max_key    FROM ojb_hl_seq where tablename='SEQ_FRAGMENT'), 'org.apache.jetspeed.om.page.impl.FragmentImpl', (SELECT temp_value FROM ingrid_temp WHERE temp_key = 'default_page_fragment_id'), 'ingrid-portal-apps::SearchSimple',        'portlet', 0, 0, -1, -1, -1, -1, -1);
INSERT INTO fragment (fragment_id, class_name, parent_id, name, type, layout_row, layout_column, layout_x, layout_y, layout_z, layout_width, layout_height) VALUES ((SELECT max_key+1  FROM ojb_hl_seq where tablename='SEQ_FRAGMENT'), 'org.apache.jetspeed.om.page.impl.FragmentImpl', (SELECT temp_value FROM ingrid_temp WHERE temp_key = 'default_page_fragment_id'), 'ingrid-portal-apps::CategoryTeaser',      'portlet', 1, 0, -1, -1, -1, -1, -1);

-- Change '/_role/user/default-page.psml'
INSERT INTO ingrid_temp (temp_key, temp_value) VALUES ('role_user_default_page_fragment_id',(SELECT fragment_id FROM fragment WHERE page_id = (SELECT page_id FROM page WHERE path = '/_role/user/default-page.psml')));
DELETE FROM fragment WHERE parent_id = (SELECT temp_value FROM ingrid_temp WHERE temp_key = 'role_user_default_page_fragment_id');
INSERT INTO fragment (fragment_id, class_name, parent_id, name, type, layout_row, layout_column, layout_x, layout_y, layout_z, layout_width, layout_height) VALUES ((SELECT max_key+2  FROM ojb_hl_seq where tablename='SEQ_FRAGMENT'), 'org.apache.jetspeed.om.page.impl.FragmentImpl', (SELECT temp_value FROM ingrid_temp WHERE temp_key = 'role_user_default_page_fragment_id'), 'ingrid-portal-apps::SearchSimple',        'portlet', 0, 0, -1, -1, -1, -1, -1);
INSERT INTO fragment (fragment_id, class_name, parent_id, name, type, layout_row, layout_column, layout_x, layout_y, layout_z, layout_width, layout_height) VALUES ((SELECT max_key+3  FROM ojb_hl_seq where tablename='SEQ_FRAGMENT'), 'org.apache.jetspeed.om.page.impl.FragmentImpl', (SELECT temp_value FROM ingrid_temp WHERE temp_key = 'role_user_default_page_fragment_id'), 'ingrid-portal-apps::CategoryTeaser',      'portlet', 1, 0, -1, -1, -1, -1, -1);

-- Change '/_user/template/default-page.psml'
INSERT INTO ingrid_temp (temp_key, temp_value) VALUES ('user_template_default_page_fragment_id',(SELECT fragment_id FROM fragment WHERE page_id = (SELECT page_id FROM page WHERE path = '/_user/template/default-page.psml')));
DELETE FROM fragment WHERE parent_id = (SELECT temp_value FROM ingrid_temp WHERE temp_key = 'user_template_default_page_fragment_id');
INSERT INTO fragment (fragment_id, class_name, parent_id, name, type, layout_row, layout_column, layout_x, layout_y, layout_z, layout_width, layout_height) VALUES ((SELECT max_key+4  FROM ojb_hl_seq where tablename='SEQ_FRAGMENT'), 'org.apache.jetspeed.om.page.impl.FragmentImpl', (SELECT temp_value FROM ingrid_temp WHERE temp_key = 'user_template_default_page_fragment_id'), 'ingrid-portal-apps::SearchSimple',        'portlet', 0, 0, -1, -1, -1, -1, -1);
INSERT INTO fragment (fragment_id, class_name, parent_id, name, type, layout_row, layout_column, layout_x, layout_y, layout_z, layout_width, layout_height) VALUES ((SELECT max_key+5  FROM ojb_hl_seq where tablename='SEQ_FRAGMENT'), 'org.apache.jetspeed.om.page.impl.FragmentImpl', (SELECT temp_value FROM ingrid_temp WHERE temp_key = 'user_template_default_page_fragment_id'), 'ingrid-portal-apps::CategoryTeaser',      'portlet', 1, 0, -1, -1, -1, -1, -1);

-- Change '/main-search.psml'
INSERT INTO ingrid_temp (temp_key, temp_value) VALUES ('main_search_fragment_id',(SELECT fragment_id FROM fragment WHERE page_id = (SELECT page_id FROM page WHERE path = '/main-search.psml')));
DELETE FROM fragment WHERE parent_id = (SELECT temp_value FROM ingrid_temp WHERE temp_key = 'main_search_fragment_id');
INSERT INTO fragment (fragment_id, class_name, parent_id, name, type, decorator, layout_row, layout_column, layout_x, layout_y, layout_z, layout_width, layout_height) VALUES ((SELECT max_key+13  FROM ojb_hl_seq where tablename='SEQ_FRAGMENT'), 'org.apache.jetspeed.om.page.impl.FragmentImpl', (SELECT temp_value FROM ingrid_temp WHERE temp_key = 'main_search_fragment_id'), 'ingrid-portal-apps::SearchSimpleResult',  'portlet', 'ingrid-clear', 0, 0, -1, -1, -1, -1, -1);
INSERT INTO fragment (fragment_id, class_name, parent_id, name, type, layout_row, layout_column, layout_x, layout_y, layout_z, layout_width, layout_height)            VALUES ((SELECT max_key+14  FROM ojb_hl_seq where tablename='SEQ_FRAGMENT'), 'org.apache.jetspeed.om.page.impl.FragmentImpl', (SELECT temp_value FROM ingrid_temp WHERE temp_key = 'main_search_fragment_id'), 'ingrid-portal-apps::SearchResult',        'portlet', 1, 0, -1, -1, -1, -1, -1);

-- Hide '/language.link'
UPDATE link SET is_hidden = 1 WHERE path = '/language.link';

-- Delete all '/_user/<USER>/default-page.psml'
DELETE FROM page WHERE PATH LIKE '/_user/%/default-page.psml' AND NOT PATH = '/_user/template/default-page.psml';

-- Delete temporary table
DROP TABLE IF EXISTS ingrid_temp;

-- update max keys
UPDATE ojb_hl_seq SET max_key=max_key+grab_size+grab_size, version=version+1 WHERE tablename='SEQ_FRAGMENT';