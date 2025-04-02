CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       phone VARCHAR(20),
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       profile_picture VARCHAR(1000),
                       registration_date DATE NOT NULL,
                       role VARCHAR(50),
                       daily_calories DOUBLE NOT NULL
);
CREATE TABLE meal_catalogs (
                               id UUID PRIMARY KEY,
                               name VARCHAR(255) NOT NULL,
                               description TEXT NOT NULL,
                               added_on DATE NOT NULL,
                               type VARCHAR(50) NOT NULL,
                               owner_id UUID NOT NULL,
                               FOREIGN KEY (owner_id) REFERENCES users(id)
);
CREATE TABLE meals (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description TEXT NOT NULL,
                       proteins DECIMAL(10,2) NOT NULL,
                       carbs DECIMAL(10,2) NOT NULL,
                       fats DECIMAL(10,2) NOT NULL,
                       total_calories DECIMAL(10,2) NOT NULL,
                       added_on DATE NOT NULL,
                       picture VARCHAR(1000) NOT NULL,
                       owner_id UUID,
                       meal_catalog_id UUID,
                       FOREIGN KEY (owner_id) REFERENCES users(id),
                       FOREIGN KEY (meal_catalog_id) REFERENCES meal_catalogs(id)
);

CREATE TABLE comments (
                          id UUID PRIMARY KEY,
                          text TEXT NOT NULL,
                          created_on DATE,
                          user_id UUID,
                          meal_id UUID,
                          FOREIGN KEY (user_id) REFERENCES users(id),
                          FOREIGN KEY (meal_id) REFERENCES meals(id)
);

CREATE TABLE up_votes (
                          id UUID PRIMARY KEY,
                          date DATE NOT NULL,
                          user_id UUID NOT NULL,
                          meal_id UUID NOT NULL,
                          FOREIGN KEY (user_id) REFERENCES users(id),
                          FOREIGN KEY (meal_id) REFERENCES meals(id)
);

CREATE TABLE reports (
                         id UUID PRIMARY KEY,
                         troublemaker VARCHAR(255) NOT NULL,
                         report_type VARCHAR(50),
                         where_it_happened VARCHAR(50),
                         description TEXT NOT NULL,
                         date_of_issue DATE NOT NULL,
                         created_on DATE NOT NULL,
                         reviewed BOOLEAN,
                         concerned_user_id UUID,
                         FOREIGN KEY (concerned_user_id) REFERENCES users(id)
);

CREATE TABLE winners (
                         id UUID PRIMARY KEY,
                         user_id UUID,
                         meal_id UUID,
                         FOREIGN KEY (user_id) REFERENCES users(id),
                         FOREIGN KEY (meal_id) REFERENCES meals(id)
);