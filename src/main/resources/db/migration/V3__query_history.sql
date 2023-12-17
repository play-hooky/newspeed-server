create table newspeed.query_history
(
    id         bigint auto_increment comment '검색 기록 ID' primary key,
    user_id    bigint                             not null comment '사용자 ID',
    query      varchar(256)                       not null comment '검색어',
    created_at datetime default CURRENT_TIMESTAMP not null comment '생성일시',
    updated_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '수정일시',
    deleted_at datetime                           null comment '삭제일시',
    constraint query_history_user_id_fk
        foreign key (user_id) references user (id)
)
    comment '사용자 검색 기록 관리 테이블';

