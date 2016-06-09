Purpose
=======

This project generates gotv PDFs of all electoral districts (wards) for backup
purposes.

For performance, only the active wards (those with voting intentions recorded in the voter database)
are queried, e.g.

select distinct v.ward_code from voters v
join voting s on s.voter_id = v.id
where s.intention >= 4

