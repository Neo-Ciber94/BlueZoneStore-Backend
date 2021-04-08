USE BlueZoneStore;

CREATE TABLE Product (
id int primary key not null identity(1,1),
[name] nvarchar(max) not null,
[description] nvarchar(max) null,
price money not null,
available int not null,

constraint chk_product_price_is_positive check(price > 0),
constraint chk_product_available_is_positive check(available > 0),
)