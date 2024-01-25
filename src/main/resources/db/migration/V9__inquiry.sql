create table newspeed.inquiry_question
(
    id         bigint auto_increment comment 'id' primary key ,
    user_id    bigint                             not null comment '사용자 id',
    title      varchar(256)                       not null comment '문의 내용 제목',
    body       text                               not null comment '본문 내용',
    created_at datetime default current_timestamp not null comment '생성한 시간',
    updated_at datetime default current_timestamp not null on update current_timestamp comment '수정한 시간',
    deleted_at datetime                           null comment '삭제한 시간'
)
    comment '사용자 문의 질문';

create index inquiry_question_user_id_index
    on newspeed.inquiry_question (user_id);

create table newspeed.inquiry_answer
(
    id          bigint auto_increment comment 'id' primary key ,
    question_id bigint                             not null comment '문의 내용 id',
    body        text                               not null comment '문의 답변',
    created_by  varchar(64)                        not null comment '답변한 사람',
    created_at  datetime default current_timestamp not null comment '생성한 시간',
    updated_at  datetime default current_timestamp not null on update current_timestamp comment '수정한 시간',
    deleted_at  datetime                           null comment '삭제한 시간'
)
    comment '사용자 문의 답변';

create index inquiry_answer_question_id_index
    on newspeed.inquiry_answer (question_id);

