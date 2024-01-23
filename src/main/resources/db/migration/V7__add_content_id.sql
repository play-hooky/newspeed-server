alter table newspeed.content
    add content_id_in_platform varchar(128) not null comment '각 플랫폼에서 할당한 컨텐츠 ID' after user_id;

create index content_user_id_content_id_in_platform_index
    on newspeed.content (user_id, content_id_in_platform);