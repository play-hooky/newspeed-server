alter table newspeed.query_history
    add platform enum ('INSTAGRAM', 'YOUTUBE', 'NEWSPEED') not null comment 'SNS 플랫폼 INSTAGRAM, YOUTUBE' after query;