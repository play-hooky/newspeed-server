create table newspeed.alarm
(
    id         bigint auto_increment comment 'id' primary key,
    user_id    bigint                             not null comment '사용자 id',
    start_time time                               not null comment '알람 시작 시간',
    end_time   time                               not null comment '알람 종료 시간',
    created_at datetime default CURRENT_TIMESTAMP not null comment '생성한 시간',
    updated_at datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '수정한 시간',
    deleted_at datetime                           null comment '삭제한 시간',
    is_deleted tinyint(1) as (if((`deleted_at` is not null), 1, 0)) not null comment '삭제 여부',
    constraint user_id unique (user_id, is_deleted)
)
    comment '사용자 알림 시간 관리';

