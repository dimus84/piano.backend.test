drop schema if exists piano.backend.test;
create schema piano.backend.test;
grant all privileges on piano.backend.test.* to 'piano.backend.test'@'localhost' identified by 'piano.backend.test';
grant all privileges on piano.backend.test.* to 'piano.backend.test'@'%' identified by 'piano.backend.test';

