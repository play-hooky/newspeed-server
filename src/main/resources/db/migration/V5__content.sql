create table newspeed.content
(
    id         bigint auto_increment comment 'id'
        primary key,
    user_id    bigint                                    not null comment '사용자 ID',
    url        varchar(512)                              not null comment '컨텐츠 url',
    platform   enum ('YOUTUBE', 'INSTAGRAM', 'NEWSPEED') not null comment 'SNS 플랫폼',
    created_at datetime default CURRENT_TIMESTAMP        not null comment '생성한 시간',
    updated_at datetime default CURRENT_TIMESTAMP        not null on update CURRENT_TIMESTAMP comment '수정한 시간',
    deleted_at datetime                                  null comment '삭제한 시간'
)
    comment '사용자 컨텐츠 보관함';

create index content_user_id_index
    on newspeed.content (user_id);

