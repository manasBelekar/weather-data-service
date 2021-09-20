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
  country_code VARCHAR(20),
  timestamp NUMERIC NOT NULL,
  CONSTRAINT unq_weather_record UNIQUE (city_id, timestamp)
);


--Test data for check
insert into weather values (10001,15,1008,FALSE,801,'few clouds',2950159,'Berlin','DE',1632060361);
insert into weather values (10002,18,1008,FALSE,801,'few clouds',2950159,'Berlin','DE',1632061152);

