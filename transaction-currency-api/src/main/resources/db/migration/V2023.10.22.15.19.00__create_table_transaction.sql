create table if not exists transactions
(
    id                  uuid primary key,
    description         varchar(50),
    transaction_date    date not null,
    purchase_amount     numeric(10, 2) not null
        CHECK (purchase_amount >= 0 AND ROUND(purchase_amount, 2) = purchase_amount)
);