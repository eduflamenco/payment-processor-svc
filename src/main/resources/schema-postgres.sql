create table transactions_history
(
    id  text primary key,
    from_account_number varchar(19) not null,
    receiver_account_number   varchar(19) not null,
    amount              numeric        default 0.00,
    client_id           varchar(25)     not null,
    status 	varchar(15) not null,
    created_date        timestamp   not null default now()
);