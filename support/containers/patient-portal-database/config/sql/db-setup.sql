drop table if exists bills;
drop table if exists appointments;
drop table if exists appointment_requests;
drop table if exists patients;
drop table if exists doctors;

create table patients (
    id                  serial primary key,
    name                varchar not null,
    zip                 varchar,
    phone               varchar,
    email               varchar
);

create table doctors (
    id                  serial primary key,
    name                varchar not null,
    phone               varchar,
    email               varchar
);

create table appointments (
    id                  serial primary key,
    doctor_id           integer not null references doctors,
    patient_id          integer not null references patients,
    date                date,
    time                time
);

create table appointment_requests (
    id                  serial primary key,
    patient_id          integer not null references patients,
    appointment_id      integer references appointments,
    date                date not null,
    time                time not null
);

create table bills (
    id                  serial primary key,
    patient_id          integer not null references patients,
    summary             varchar,
    amount              integer not null default 0,
    date_paid           date
);

create or replace function notify_changes() returns trigger as $$
declare
begin
    raise warning 'Changes!';
    notify changes;
    return new;
end;
$$ language plpgsql;

create trigger patients_changes
after insert or update or delete or truncate on patients
execute procedure notify_changes();

create trigger doctors_changes
after insert or update or delete or truncate on doctors
execute procedure notify_changes();

create trigger appointments_changes
after insert or update or delete or truncate on appointments
execute procedure notify_changes();

create trigger appointment_requests_changes
after insert or update or delete or truncate on appointment_requests
execute procedure notify_changes();

create trigger bill_changes
after insert or update or delete or truncate on bills
execute procedure notify_changes();

insert into patients
  (name, zip, phone, email)
values
  ('Angela Martin', '01821', '206-455-7225', 'monkey@example.net'),
  ('Dwight Schrute', '02143', '555-102-3087', 'recyclops@example.net'),
  ('Jim Halpert', '98823', '617-234-5678', 'bigtuna@example.net'),
  ('Kelly Kapoor', '12345', '555-781-6723', 'businessb@example.net'),
  ('Kevin Malone', '12345', '555-123-3345', 'cookiemonster@example.net'),
  ('Michael Scott', '12345', '555-987-2345', 'scarn@example.net'),
  ('Oscar Martinez', '12345', '555-555-5555', 'actually@example.net'),
  ('Pam Beesly', '02474', '509-213-9901', 'pampam@example.net'),
  ('Ryan Howard', '88642', '274-754-2798', 'temp@example.net'),
  ('Toby Flenderson', '99891', '555-278-0870', 'ss@example.net');

insert into doctors
  (name, phone, email)
values
  ('Benjamin Pierce', '555-555-1001', 'hawkeye@example.net'),
  ('Beverly Crusher', '555-555-1002', 'gates@example.net'),
  ('Doogie Howser', '555-555-1003', 'neil@example.net'),
  ('Leonard McCoy', '555-555-1004', 'bones@example.net'),
  ('Michaela Quinn', '555-555-1005', 'drmike@example.net'),
  ('Miranda Bailey', '555-555-1006', 'chief@example.net');

insert into bills (patient_id, summary, amount)
values (1, 'Knee surgery', 50);
