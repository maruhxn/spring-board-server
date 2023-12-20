create table if not exists comment
(
    comment_id
    bigint
    generated
    by
    default as
    identity,
    content
    TEXT
    not
    null,
    created_at
    timestamp
(
    6
),
    member_id bigint,
    post_id bigint,
    primary key
(
    comment_id
)
    );

create table if not exists member
(
    member_id
    bigint
    generated
    by
    default as
    identity,
    created_at
    timestamp
(
    6
),
    updated_at timestamp
(
    6
),
    email varchar
(
    30
) not null,
    password varchar
(
    255
) not null,
    profile_image TEXT not null,
    role varchar
(
    255
) default 'ROLE_USER' check
(
    role
    in
(
    'ROLE_USER',
    'ROLE_ADMIN'
)),
    username varchar
(
    10
) not null,
    primary key
(
    member_id
)
    );

create table if not exists post
(
    post_id
    bigint
    generated
    by
    default as
    identity,
    created_at
    timestamp
(
    6
),
    updated_at timestamp
(
    6
),
    content TEXT not null,
    title varchar
(
    255
) not null,
    view_count bigint default 0 not null,
    member_id bigint,
    primary key
(
    post_id
)
    );

create table if not exists post_image
(
    post_image_id
    bigint
    generated
    by
    default as
    identity,
    created_at
    timestamp
(
    6
),
    original_name varchar
(
    255
) not null,
    stored_name TEXT not null,
    post_id bigint,
    primary key
(
    post_image_id
)
    );

create index idx__email__username
    on member (email, username);

alter table if exists member
drop
constraint if exists UK_mbmcqelty0fbrvxp1q58dn57t;

alter table if exists member
    add constraint UK_mbmcqelty0fbrvxp1q58dn57t unique (email);

alter table if exists member
drop
constraint if exists UK_gc3jmn7c2abyo3wf6syln5t2i;

alter table if exists member
    add constraint UK_gc3jmn7c2abyo3wf6syln5t2i unique (username);

alter table if exists comment
    add constraint FKmrrrpi513ssu63i2783jyiv9m
    foreign key (member_id)
    references member;

alter table if exists comment
    add constraint FKs1slvnkuemjsq2kj4h3vhx7i1
    foreign key (post_id)
    references post;

alter table if exists post
    add constraint FK83s99f4kx8oiqm3ro0sasmpww
    foreign key (member_id)
    references member;

alter table if exists post_image
    add constraint FKsip7qv57jw2fw50g97t16nrjr
    foreign key (post_id)
    references post;