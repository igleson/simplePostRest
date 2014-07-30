# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  id                        bigint not null,
  post_id                   bigint not null,
  coment                    varchar(255) not null,
  constraint pk_comment primary key (id))
;

create table post (
  id                        bigint not null,
  msg                       varchar(255) not null,
  constraint pk_post primary key (id))
;

create sequence comment_seq;

create sequence post_seq;

alter table comment add constraint fk_comment_post_1 foreign key (post_id) references post (id) on delete restrict on update restrict;
create index ix_comment_post_1 on comment (post_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists comment;

drop table if exists post;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists comment_seq;

drop sequence if exists post_seq;

