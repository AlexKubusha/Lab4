CREATE TABLE IF NOT EXISTS books (
    id SERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL, -- Дискримінатор (Book, EBook, PaperBook, AudioBook, RareBook)
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    year_published INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,

    -- Специфічні поля (Nullable, бо вони є не у всіх книг)
    file_size_mb DOUBLE PRECISION,   -- для EBook
    weight_grams INT,                -- для PaperBook
    duration_minutes INT,            -- для AudioBook
    book_condition VARCHAR(100)      -- для RareBook
    );