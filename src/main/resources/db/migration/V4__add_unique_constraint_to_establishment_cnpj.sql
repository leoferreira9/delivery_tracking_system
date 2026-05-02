ALTER TABLE establishments
ADD CONSTRAINT uk_establishments_cnpj UNIQUE(cnpj);