create table if not exists comment
(
    comment_id
    bigint
    not
    null
    auto_increment,
    created_at
    datetime
(
    6
),
    member_id bigint,
    post_id bigint,
    content TEXT not null,
    primary key
(
    comment_id
)
    ) engine=InnoDB;

create table if not exists member
(
    created_at datetime
(
    6
),
    member_id bigint not null auto_increment,
    updated_at datetime
(
    6
),
    username varchar
(
    10
) not null,
    email varchar
(
    30
) not null,
    password varchar
(
    255
) not null,
    profile_image TEXT not null,
    role enum
(
    'ROLE_ADMIN',
    'ROLE_USER'
) default 'ROLE_USER',
    primary key
(
    member_id
)
    ) engine=InnoDB;

create table if not exists post
(
    created_at datetime
(
    6
),
    member_id bigint,
    post_id bigint not null auto_increment,
    updated_at datetime
(
    6
),
    view_count bigint default 0 not null,
    content TEXT not null,
    title varchar
(
    255
) not null,
    primary key
(
    post_id
)
    ) engine=InnoDB;

create table if not exists post_image
(
    created_at datetime
(
    6
),
    post_id bigint,
    post_image_id bigint not null auto_increment,
    original_name varchar
(
    255
) not null,
    stored_name TEXT not null,
    primary key
(
    post_image_id
)
    ) engine=InnoDB;

alter table member
    add constraint UK_gc3jmn7c2abyo3wf6syln5t2i unique (username);

alter table member
    add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (email);

alter table comment
    add constraint FKmrrrpi513ssu63i2783jyiv9m
        foreign key (member_id)
            references member (member_id);

alter table comment
    add constraint FKs1slvnkuemjsq2kj4h3vhx7i1
        foreign key (post_id)
            references post (post_id);

alter table post
    add constraint FK83s99f4kx8oiqm3ro0sasmpww
        foreign key (member_id)
            references member (member_id);

alter table post_image
    add constraint FKsip7qv57jw2fw50g97t16nrjr
        foreign key (post_id)
            references post (post_id);