use `zhao-picture` ;
ALTER TABLE `zhao-picture`.space
    ADD COLUMN space_type int default 0 not null comment '空间类型：0-私有 1-团队';
CREATE INDEX idx_spaceType ON `zhao-picture`.space (space_type);
