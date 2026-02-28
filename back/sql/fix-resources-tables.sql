-- First, remove the foreign key constraint from resource_attachment table
ALTER TABLE resource_attachment DROP FOREIGN KEY FK9le5bljs7mdlmpme3vm79m4wq;

-- Insert recent learning resources into learning_resources table
INSERT INTO learning_resources (title, type, category, author, upload_date, views, status, url, description, content, upload_by) VALUES 
('2025 Central Economic Work Conference', 'video', 'policy', 'Central Party School', '2025-12-15', 0, 'published', NULL, 'Authoritative video course on 2025 Central Economic Work Conference', '2025 Central Economic Work Conference was held in Beijing from December 12 to 14.', NULL),
('Party Member Education Manual', 'document', 'theory', 'Party Building Press', '2025-12-10', 0, 'published', NULL, 'Important reference for party member education in new development stage', 'This manual helps party members learn theoretical knowledge in new development stage.', NULL),
('Technology Innovation and High-Quality Development', 'video', 'model', 'Science and Technology Institute', '2025-11-28', 0, 'published', NULL, 'Special lecture on technology innovation driving high-quality development', 'This lecture discusses the important role of technology innovation in economic development.', NULL),
('Party Building Innovation in Digital Economy Era', 'article', 'theory', 'Digital Party Building Center', '2025-11-20', 0, 'published', NULL, 'Practice guide for party building innovation in digital economy era', 'This article analyzes challenges and opportunities for party building in digital economy era.', NULL);

-- Drop the old learning_resource table
DROP TABLE IF EXISTS learning_resource;
