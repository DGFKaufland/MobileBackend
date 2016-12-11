INSERT INTO message (id, text) VALUES ('1', 'Beratung in {department} gewünscht');
INSERT INTO message (id, text) VALUES ('2', 'Feuer in {department}');
INSERT INTO message (id, text) VALUES ('3', '{contact} braucht deine Hilfe');
INSERT INTO message (id, text) VALUES ('4', 'Neuer LKW am Wareneingang angekommen');
INSERT INTO message (id, text) VALUES ('5', '{contact} ist ab sofort in {department} verfügbar');
INSERT INTO message (id, text) VALUES ('6', 'Kühltruhe 0815 ist seit über 30 Sekunden geöffnet');
INSERT INTO message (id, text) VALUES ('7', 'Bitte zur Information kommen');
INSERT INTO message (id, text) VALUES ('8', 'Bitte zum Leergut kommen');
INSERT INTO message (id, text) VALUES ('9', 'Rückruf {number}');
INSERT INTO message (id, text) VALUES ('10', 'Backautomat 4 ist fertig');

INSERT INTO contact (id, name, registration_token, os, is_available) VALUES ('1', 'Max Mustermann',   'asdf213as', '1', true);
INSERT INTO contact (id, name, registration_token, os, is_available) VALUES ('2', 'Tom Heinrich',     'ASDASDASD', '0', true);
INSERT INTO contact (id, name, registration_token, os, is_available) VALUES ('3', 'Thomas Wolff',  'ASD123aDS', '1', false);
INSERT INTO contact (id, name, registration_token, os, is_available) VALUES ('4', 'Arne Friedrich',   '123ASD214', '0', true);
INSERT INTO contact (id, name, registration_token, os, is_available) VALUES ('5', 'Franko Santo',     '123ASD132', '1', true);
INSERT INTO contact (id, name, registration_token, os, is_available) VALUES ('6', 'Timo Dittmann',    'eVFRnGfyj74:APA91bGOMcdCsXbhrlXjnLle5mbvlHz-Y639Q370Cqn7melIoAezI7vhxtk4tWmW6TrxIRTfRiH6mOxaFYuZ34Buyjd6I_6DN6CZ0AMFtpwolQWCFILma_Ac0OE70te7Xp29oW0At7zX', '0', true);

INSERT INTO notification (id, to_contact_id, from_contact_id, from_contact_name, body, state) VALUES ('1', '6', '6', 'Timo Dittmann', 'Bitte Kasse 2 aufmachen', '0');
INSERT INTO notification (id, to_contact_id, from_contact_id, from_contact_name, body, state) VALUES ('2', '4', '3', 'Max und Moritz', 'Bitte zur Information kommen', '1');
INSERT INTO notification (id, to_contact_id, from_contact_id, from_contact_name, body, state) VALUES ('3', '6', '6', 'Timo Dittmann', 'Beratung in Obst und Gemüse gewünscht', '0');
INSERT INTO notification (id, to_contact_id, from_contact_id, from_contact_name, body, state) VALUES ('4', '1', '1', 'Max Mustermann', 'Brand im Lager, bitte löschen!', '1');
INSERT INTO notification (id, to_contact_id, from_contact_id, from_contact_name, body, state) VALUES ('5', '5', '1', 'Max Musterfrau', 'Rückruf 42', '2');

INSERT INTO department (id, name) VALUES('1', 'Obst und Gemüse');
INSERT INTO department (id, name) VALUES('2', 'Süßwaren');
INSERT INTO department (id, name) VALUES('3', 'Getränke, Spirituosen');
INSERT INTO department (id, name) VALUES('4', 'MoPro');
INSERT INTO department (id, name) VALUES('5', 'Fleisch, Geflügel, Wurst');
INSERT INTO department (id, name) VALUES('6', 'Fisch');
INSERT INTO department (id, name) VALUES('7', 'Drogerie');