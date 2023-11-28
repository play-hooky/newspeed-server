create table newspeed.user
(
    id                bigint auto_increment comment '사용자ID '
        primary key,
    email             varchar(128)                       not null comment '사용자 이메일',
    nickname          varchar(128)                       not null comment '사용자 닉네임',
    platform          enum ('KAKAO', 'APPLE')            not null comment 'SNS 플랫폼 KAKAO, APPLE',
    profile_image_url varchar(256) charset utf8          not null comment '프로필 사진 url ',
    role              enum ('USER', 'ADMIN')             not null comment '사용자 권한 USER, ADMIN',
    created_at        datetime default CURRENT_TIMESTAMP not null comment '생성일시',
    updated_at        datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '수정일시',
    deleted_at        datetime                           null comment '삭제일시',
    constraint user_pk2
        unique (email, platform)
)
    comment '사용자 정보 관리 테이블';