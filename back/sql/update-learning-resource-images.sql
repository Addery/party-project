-- 更新学习资源图片为红色党建相关图片
USE party_management_system;

-- 更新学习资源图片URL
UPDATE learning_resource SET image = 'https://img.588ku.com/thumb_200_0_100/c5/29/8d/44/c5298d449906b816d7025d06fa9b3772.jpg!/quality/90/format/jpg' WHERE id = 1;
UPDATE learning_resource SET image = 'https://img.588ku.com/thumb_200_0_100/37/5e/60/25/375e602587b914a4572a0211f1839792.jpg!/quality/90/format/jpg' WHERE id = 2;
UPDATE learning_resource SET image = 'https://img.588ku.com/thumb_200_0_100/5e/3a/1d/0d/5e3a1d0d9f5c3a9b9f3c3a9b9f3c3a9b.jpg!/quality/90/format/jpg' WHERE id = 3;
UPDATE learning_resource SET image = 'https://img.588ku.com/thumb_200_0_100/4e/2a/3d/4b/4e2a3d4b8c5d3a9b9f3c3a9b9f3c3a9b.jpg!/quality/90/format/jpg' WHERE id = 4;
UPDATE learning_resource SET image = 'https://img.588ku.com/thumb_200_0_100/3e/1a/2d/3b/3e1a2d3b7c4d3a9b9f3c3a9b9f3c3a9b.jpg!/quality/90/format/jpg' WHERE id = 5;
UPDATE learning_resource SET image = 'https://img.588ku.com/thumb_200_0_100/2e/0a/1d/2b/2e0a1d2b6c3d3a9b9f3c3a9b9f3c3a9b.jpg!/quality/90/format/jpg' WHERE id = 6;

-- 为了更丰富的党建图片，添加更多示例图片
UPDATE learning_resource SET image = 'https://img.588ku.com/thumb_200_0_100/1e/f9/0d/1b/1ef90d1b5c2d3a9b9f3c3a9b9f3c3a9b.jpg!/quality/90/format/jpg' WHERE id = 7;
UPDATE learning_resource SET image = 'https://img.588ku.com/thumb_200_0_100/0e/e9/fd/0b/0ee9fd0b4c1d3a9b9f3c3a9b9f3c3a9b.jpg!/quality/90/format/jpg' WHERE id = 8;
UPDATE learning_resource SET image = 'https://img.588ku.com/thumb_200_0_100/fe/d9/ed/fb/fed9edfb3c0d3a9b9f3c3a9b9f3c3a9b.jpg!/quality/90/format/jpg' WHERE id = 9;
UPDATE learning_resource SET image = 'https://img.588ku.com/thumb_200_0_100/ee/c9/dd/eb/eec9ddeb2bfd3a9b9f3c3a9b9f3c3a9b.jpg!/quality/90/format/jpg' WHERE id = 10;

COMMIT;
