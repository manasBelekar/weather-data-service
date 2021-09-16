DROP TABLE IF EXISTS public.weather;

CREATE TABLE IF NOT EXISTS public.weather(
  id INT AUTO_INCREMENT PRIMARY KEY,
  temp NUMERIC NOT NULL,
  pressure NUMERIC NOT NULL,
  umbrella BOOLEAN DEFAULT FALSE,
  weather_id NUMERIC,
  weather_desc VARCHAR(255),
  city_id NUMERIC,
  city_name VARCHAR(255),
  timestamp timestamp NOT NULL,
);
