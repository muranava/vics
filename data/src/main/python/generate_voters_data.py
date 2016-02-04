from faker import Faker
import datetime
import uuid

fake = Faker()

template = 'INSERT INTO electors(id,ward_code,polling_district,elector_id,elector_suffix,title,first_name,last_name,' \
           'initial,dob,flag,modified,created) ' \
           'VALUES(\'{}\',\'{}\',\'{}\',\'{}\',\'{}\',\'{}\',\'{}\',\'{}\',\'{}\',\'{}\',\'{}\',\'{}\',\'{}\');'

for num in range(0, 100):
    dt = datetime.datetime.today()
    name = fake.name()
    name_parts = name.split(" ")
    ward_code = str(uuid.uuid4())[0:3]
    polling_district = str(uuid.uuid4())[0:3]
    elector_id = str(uuid.uuid4())[0:3]
    elector_suffix = str(uuid.uuid4())[0:2]
    print template.format(uuid.uuid4(), ward_code, polling_district, elector_id, elector_suffix, '', name_parts[0],
                          name_parts[1], name_parts[0][0], '1980-09-24', 'F', dt, dt)

