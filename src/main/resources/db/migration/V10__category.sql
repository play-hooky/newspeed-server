create table newspeed.category
(
    id         bigint auto_increment comment '검색 기록 ID' primary key,
    user_id    bigint                             not null comment '사용자 ID',
    name      varchar(256)                       not null comment '카테고리',
    platform enum ('INSTAGRAM', 'YOUTUBE', 'NEWSPEED') not null comment 'SNS 플랫폼 INSTAGRAM, YOUTUBE',
    created_at datetime default CURRENT_TIMESTAMP not null comment '생성일시',
    updated_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '수정일시',
    deleted_at datetime                           null comment '삭제일시'
)
    comment '사용자 검색 기록 관리 테이블';

create index category_category_index
    on newspeed.category (user_id);

