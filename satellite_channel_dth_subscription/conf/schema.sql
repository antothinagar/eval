
CREATE TABLE `users`
(
    `id`        int(11)     NOT NULL AUTO_INCREMENT,
    `name`      varchar(50) NOT NULL,
    `email_id`  varchar(50) NOT NULL,
    `phone`     varchar(15),
    `password`  varchar(50) NOT NULL,
    `is_active` BOOLEAN DEFAULT false,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_email` (`email_id`)
);


CREATE TABLE `channels`
(
    `id`       bigint(11)   NOT NULL AUTO_INCREMENT,
    `name`     varchar(200) NOT NULL,
    `cost`     varchar(200) NOT NULL,
    `language` varchar(200) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `packages`
(
    `id`   bigint(11)   NOT NULL AUTO_INCREMENT,
    `name` varchar(200) NOT NULL,
    UNIQUE KEY `unique_name` (`name`)
);



CREATE TABLE `plan`
(
    `id`         int(11)      NOT NULL AUTO_INCREMENT,
    `price`      int(50),
    `package_id` int(11),
    `duration`   varchar(200),
    PRIMARY KEY (`id`),
    CONSTRAINT `packagePlan_fkey` FOREIGN KEY (`package_id`) REFERENCES `packages` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `user_plan`
(
    `id`      int(11)   NOT NULL AUTO_INCREMENT,
    `user_id` int(11)   NOT NULL,
    `plan_id` int(11)   NOT NULL,
    `created` TIMESTAMP NOT NULL,
    `updated` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `userPlan_fKey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `plan_fkey` FOREIGN KEY (`plan_id`) REFERENCES `plan` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE `package_channel`
(
    `package_id` int(11) NOT NULL,
    `channel_id` int(11) NOT NULL,
    CONSTRAINT `package_fKey` FOREIGN KEY (`package_id`) REFERENCES `packages` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `channel_fkey` FOREIGN KEY (`channel_id`) REFERENCES `channels` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);


CREATE TABLE `subscription`
(
    `id`       int(11)      NOT NULL AUTO_INCREMENT,
    `name`     varchar(200) NOT NULL,
    `user_id`  int(11)      NOT NULL,
    `created`  TIMESTAMP    NOT NULL ,
    `end_date` TIMESTAMP    NOT NULL ,
    PRIMARY KEY (`id`),
    CONSTRAINT `user_fkey` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);



