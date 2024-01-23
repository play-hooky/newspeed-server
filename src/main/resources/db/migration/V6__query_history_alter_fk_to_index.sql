create index query_history_user_id_index
    on newspeed.query_history (user_id);

alter table newspeed.query_history
    drop foreign key query_history_user_id_fk;