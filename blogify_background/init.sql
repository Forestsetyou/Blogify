-- 用于博客项目 Blogify 的数据库

-- 关闭外键约束
SET FOREIGN_KEY_CHECKS = 0;

-- 新建数据库 blogify
CREATE DATABASE IF NOT EXISTS blogify;
USE blogify;

-- 建表-博客分类 types
CREATE TABLE IF NOT EXISTS types(
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- 数据
INSERT INTO types values(1, "工具使用");
INSERT INTO types values(2, "问题解决");
INSERT INTO types values(3, "语言学习");
INSERT INTO types values(4, "操作系统");
INSERT INTO types values(5, "算法");
INSERT INTO types values(6, "深度学习");
INSERT INTO types values(7, "随笔");

-- 建表-博客标签 tags
CREATE TABLE IF NOT EXISTS tags(
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- 数据
INSERT INTO tags values(1, "CTF-WP");
INSERT INTO tags values(2, "CTF-Notes");
INSERT INTO tags values(3, "Linux");
INSERT INTO tags values(4, "配置与说明");
INSERT INTO tags values(5, "离散数学");
INSERT INTO tags values(6, "JAVA");
INSERT INTO tags values(7, "C++");
INSERT INTO tags values(8, "木马与免杀");
INSERT INTO tags values(9, "Web开发");
INSERT INTO tags values(10, "Windows");

-- 建表-博文标签映射 essay_tag_mapping
CREATE TABLE IF NOT EXISTS essay_tag_mapping(
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    essay_id int(11) NOT NULL,
    tag_id int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- 数据
INSERT INTO essay_tag_mapping values(1, 1, 3);
INSERT INTO essay_tag_mapping values(2, 2, 6);
INSERT INTO essay_tag_mapping values(3, 3, 10);
INSERT INTO essay_tag_mapping values(4, 3, 4);
INSERT INTO essay_tag_mapping values(5, 4, 4);
INSERT INTO essay_tag_mapping values(6, 4, 2);

-- 建表-用户 users
CREATE TABLE IF NOT EXISTS users(
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    role_id int(11) NOT NULL,
    avatar_pic_id int(11) DEFAULT NULL,
    introductions text,
    email varchar(127) DEFAULT "",
    gender varchar(31) DEFAULT NULL,
    nickname varchar(255) DEFAULT NULL,
    username varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    salt varchar(255) DEFAULT NULL,
    create_time datetime NOT NULL,
    latest_active_time datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- 数据
INSERT INTO users values(1, 1, NULL, "管理员啦", "admin@festu.blogify.com", "武装直升机", "The Greatest Admin", "admin", "5d41e2c37f2b67da9bffd4907b42b31a", "j+l7Mg9ZtIJEuIPdQjL0bA==", "2023-09-05 16:00:00", "2023-12-05 21:00:00");
INSERT INTO users values(2, 2, NULL, "不知名用户", "user1@blogify.com", "女", "Lovely User_1", "User1", "bef19eb58b809b6b28a144e2e4fa50f4", "HY8djiLDlVd1e1ZhNHmrEA==", "2023-09-27 17:00:00", "2023-09-27 17:00:00");
INSERT INTO users values(3, 2, NULL, "一般路过张三", "zhangsan@blogify.com", "男", "Big Zhangsan", "Zhangsan", "e2a13ba0dbb245ff3fb024c181db4eaa", "DKlpdbeiHXOTjAtd9zhdVw==", "2023-10-08 18:00:00", "2023-10-08 18:00:00");

-- 建表-角色 roles
CREATE TABLE IF NOT EXISTS roles(
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(31) NOT NULL,
    comment_permission tinyint(1) unsigned DEFAULT 1,
    essay_publish_permission tinyint(1) unsigned DEFAULT 0,
    upload_permission tinyint(1) unsigned DEFAULT 1,
    hide_essay_access_permission tinyint(1) DEFAULT 0
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- 数据
INSERT INTO roles values(1, "管理员", 1, 1, 1, 1);
INSERT INTO roles values(2, "一般用户", 1, 0, 1, 0);

-- 建表-文件 files
CREATE TABLE IF NOT EXISTS files(
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    user_id int(11) NOT NULL,
    is_publish tinyint(1) unsigned DEFAULT 0,
    `type` tinyint(2) unsigned DEFAULT NULL,
    `name` varchar(255) NOT NULL,
    description varchar(512) DEFAULT NULL,
    upload_time datetime NOT NULL,
    publish_url varchar(255) DEFAULT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- 数据
-- INSERT INTO files values(1, 1, 1, 1, "default_avatar.png", "默认头像", "2023-09-12 22:00:00", "http://localhost:7969/api/file/images/default_avatar.png"); -- 1代表图片文件


-- 建表-操作日志 operation_logs
CREATE TABLE operation_logs(
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    user_id int(11) NOT NULL,
    `type` tinyint(2) unsigned DEFAULT NULL, -- [1: "create", 2: "delete", 3: "update", 4: "search"]
    remarks varchar(255) DEFAULT NULL,
    operation_time datetime NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- 数据
INSERT INTO operation_logs values(1, 1, 1, "Blogify 诞生", "2023-09-04 19:00:00");
INSERT INTO operation_logs values(2, 1, 1, "管理员注册", "2023-09-05 16:00:00");

-- 建表-点赞
CREATE TABLE praise_mapping(
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    user_id int(11) NOT NULL,
    essay_id int(11) NOT NULL,
    is_praised tinyint(1) unsigned DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- 数据
-- INSERT INTO praise_mapping values(1, 2, 1, 1); -- 某一般用户给`Kali 下的 ld-linux.so.2`点了个赞

-- 建表-收藏 star_mapping
CREATE TABLE star_mapping(
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    user_id int(11) NOT NULL,
    essay_id int(11) NOT NULL,
    is_stared tinyint(1) unsigned DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- 数据
-- INSERT INTO star_mapping values(1, 1, 2, 1); -- 管理员收藏了'Java 笔记'

-- 建表-通知 notice_mapping
CREATE TABLE notice_mapping(
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    notifier_id int(11) NOT NULL,
    noticee_id int(11) NOT NULL,
    is_checked tinyint(1) DEFAULT 0,
    content text,
    notice_time datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO notice_mapping values(1, 1, 1, 1, "Blogify 已创建", "2023-09-04 17:00:00");
INSERT INTO notice_mapping values(2, 1, 1, 1, "管理员你好呀!", "2023-09-05 16:05:00");
INSERT INTO notice_mapping values(3, 1, 2, 0, "某一般用户你好呀!", "2023-12-14 16:00:00");

-- 建表-博客评论 comments
CREATE TABLE IF NOT EXISTS comments(
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    parent_essay_id int(11) DEFAULT NULL,
    parent_comment_id int(11) DEFAULT NULL,
    user_id int(11) NOT NULL,
    check_state tinyint(2) DEFAULT 1,
    content text,
    create_time datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 数据
INSERT INTO comments VALUES(1, 1, NULL, 1, 1, "讲的真好喵!", "2023-09-11 20:30:00");
INSERT INTO comments VALUES(2, 1, NULL, 1, 1, "原来是这样!", "2023-09-12 17:30:00");
INSERT INTO comments VALUES(3, 2, NULL, 2, 2, "加 VX 123456 购买学习课程~", "2023-10-02 19:30:00");
INSERT INTO comments VALUES(4, 3, NULL, 2, 0, "加 QQ 123456 购买JAVA全套教程视频~", "2023-11-15 15:23:00");
INSERT INTO comments VALUES(5, 4, NULL, 2, 0, "微信好像也有类似的问题呢!", "2023-12-13 17:11:25");

-- 建表-博客文章 essays
CREATE TABLE IF NOT EXISTS essays(
    id int(11) AUTO_INCREMENT PRIMARY KEY,
    type_id int(11) DEFAULT NULL,
    user_id int(11) NOT NULL,
    is_public tinyint(1) unsigned DEFAULT 1,
    background_pic_id int(11) DEFAULT NULL,
    views int(11) DEFAULT 0,
    praise int(11) DEFAULT 0,
    create_time datetime NOT NULL,
    update_time datetime NOT NULL,
    title char(255) NOT NULL,
    abstractions text,
    content text NOT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
-- 数据
INSERT INTO essays values(1, 4, 1, 1, NULL, 77230, 54336, "2023-09-10 18:00:00", "2023-09-10 18:00:00", 'Kali 下的 ld-linux.so.2', 'Kali 下的 ld-linux.so.2', '# Kali下的ld-linux.so.2\n\n### 问题描述\n\n在给Kali安装silentEye时，运行下载得到的".run"文件时提示报错：![image-20230803001154850](https://picturebed-1310517892.file.myqcloud.com/202308030041886.png)\n\n明明有这个文件但运行时却找不到。查找资料发现是系统位版本与文件位版本不匹配，分别查看版本：![image-20230803001312845](https://picturebed-1310517892.file.myqcloud.com/202308030041054.png)\n\n64位的系统所用的加载动态链接库的文件是 `/lib64/ld-linux-x86-64.so.2` ，这是个软链接，实际上是 `/lib/x86_64-linux-gnu/ld-linux-x86-64.so.2` 文件，直接用该加载器去运行".run"文件会提示：\n\n```error\n/home/sam/Public/silenteye-0.4.2.beta-ubuntu-12.04.run: error while loading shared libraries: /home/sam/Public/silenteye-0.4.2.beta-ubuntu-12.04.run: wrong ELF class: ELFCLASS32\n```\n\n通过 `readelf -l silenteye-0.4.2.beta-ubuntu-12.04.run` 查看其文件特征会发现他需要 `/lib/ld-linux.so.2` 这样一个interpreter，猜测这是与前面64位的 `ld-linux-x86-64.so.2` 对应的文件，所以需要先查找64位文件依赖的包是什么：\n\n```bash\n❯ dpkg -S ld-linux-x86-64.so.2\nlibc6:amd64: /lib64/ld-linux-x86-64.so.2\nlibc6:amd64: /lib/x86_64-linux-gnu/ld-linux-x86-64.so.2\n```\n\n该命令查找包含已安装的特定文件的包名称，可以看见是 `libc6:amd64` ，冒号后的是版本。这是64位linux的系统链接库包。\n\n先打开系统对于32位库的支持，`sudo dpkg --print-architecture` 可以查看本机支持的架构版本，此处为 `amd64` ，我们需要打开 `i386(即32位)`：\n\n```bash\ndpkg --add-architecture i386\nsudo apt update\nsudo apt dist-upgrade\n```\n\n然后查看libc6有没有对应i386的包：\n\n```bash\n❯ apt search libc6\n...\nlibc6/kali-rolling,now 2.37-5 amd64 [installed]\n  GNU C Library: Shared libraries\n\nlibc6-amd64/kali-rolling 2.37-5 i386\n  GNU C Library: 64bit Shared libraries for AMD64\n```\n\namd64已安装，安装i386的即可（忽略了下面的带"-cross"的glibc包，应该是用于交叉编译的库包，但是不知道适不适用，因此不敢乱下）：\n\n```bash\nsudo apt install libc6:i386\n```\n\n下载完毕后会在 `/lib` 下发现 `ld-linux.so.2` 这个文件，对，它就是32位系统的动态链接加载器。再次运行".run"文件发现可以成功，问题解决！\n\n---\n\n也没完全解决，这个silenteye的安装器不支持kali，因此最后安装失败= =。');
INSERT INTO essays values(2, 3, 1, 1, NULL, 80745, 49661, "2023-10-01 19:00:00", "2023-10-01 19:00:00", 'Java 笔记', 'Java 笔记', '# JAVA学习笔记\n\n\n\n## Issues\n\n1. 语句\n\n   ```java\n   FILE f=new FILE("Grades.txt");\n   ```\n\n   在IDEA中运行时，读取的目标文件不一定是源码所在目录下的"Grades.txt"，而是源码类所属的父模块中最高级的模块目录下的"Grades.txt"，但直接用系统shell运行时能争取读取源码目录下的文件。\n\n2. "FileOutputStream"等文件流创建语句必须写在"try...catch"语句块中，否则会报错\n\n3. ```java\n   Scanner.useDelimiter(pattern)\n   ```\n\n   中的pattern是正则表达式\n\n   ```\n   pattern="[...]"\n   ```\n\n   用多个分隔符代替...即可，注意最好加上`\\n\\r ` (换行回车和空格)\n\n4. `ObjectOutputStream` 所写入的数据以"ISSO-8859-1"编码\n5. `javac -encoding utf-8 DST` 以UTF-8编码编译目标类。输入内容的正确编码格式取决于输入模块的选择了什么编码进行内容输入，内容能否以正确的编码格式被读取取决于文件以什么编码格式被保存（读取），若是保存格式不对就会出现乱码。\n6. 由于 0.4 用二进制表示时是无限循环小数，因此有以下特性：\n\n   ```java\n   float x = 0.4f;\n   double y = 0.4;\n   ```\n\n   x 中实际存储的是 Float 类型的常数 0.4000000059604654（保留16位），存储在 y 中的是 Double 类型的常数 4e-16，因此 y 中的值小于 x 中的值。\n\n   ![image-20230908143010977](https://picturebed-1310517892.file.myqcloud.com/202309231348376.png)\n\n   > Qst：Float 只保存 8 位有效数字，而 5 出现在第 9 位，那为什么与 y 比较时还是大于 y？四舍五入？\n   >\n   > Ans：Float 与 Double 比较时，前者被转换成 Double 类型，由于保存的是 0.4f 也就是 FLoat 常数，因此转换时进行了精确的计算，将第九位开始的小数也保存了下来，再与 Double 类型的常数 0.4 进行比较。所以有：\n   >\n   > ```bash\n   > jshell> 0.4f - 0.4;\n   > $33 ==> 5.960464455334602E-9\n   > jshell> 0.4f - 0.4f;\n   > $34 ==> 0.0\n   > ```\n\n7. Char 类型数组"a"，使用：\n\n   ```java\n   System.out.printlb(a);\n   ```\n\n   不会输出其引用信息，而是会输出数组字符内容。\n\n   ```java\n   Sytem.out.println(""+a);\n   ```\n\n   与字符串做并置运算（+）时会输出其引用。\n\n8. Java 标识符由字母（包括各种语言的常规字符）、美元符号、下划线与数字组成，开头不能为数字。\n\n9. Java 有 8 种基本数据类型：`boolean, byte, short, int, long, float, double, char` 。');
INSERT INTO essays values(3, 2, 1, 1, NULL, 71285, 25403, "2023-11-01 20:00:00", "2023-11-01 20:00:00", 'Win10 重装 IE', 'Win10 重装 IE', '# Win10重装IE\n\n## 卸载IE\n\n主要参考：[IE浏览器的卸载](https://blog.csdn.net/qq_43701418/article/details/120150511)\n\n卸载IE分两步，在系统功能中关闭IE11的服务即可停用IE，但此时IE的文件夹还在，这也就意味着重新启用该服务即可重启IE；若想完全卸载IE还需要将IE文件夹删除。\n\nIE文件夹有两份，两个Program Files中，微软将IE的文件夹的所有者修改为特定的用户以防止该文件夹被轻易删除，因此要想卸载它需要修改该文件的权限，将其所有者设置为所有用户（Everyone），并将所有者的权限设置为完全控制，即可对文件夹进行所有操作。文件夹中的所有子文件和子文件夹的权限也需要修改，因此在修改所有者时一定要勾选"替换子容器和对象的所有者"。具体操作见参考。\n\n（删除完文件夹貌似也没有删干净，运行IE的安装程序仍然会报提示，电脑上已有新版本的IE故而无法安装。有的电脑上可以检查安装的更新中有无IE新版本的更新，但我得电脑上没有，删除注册表也无法解决该问题。）\n\n## 重装IE\n\n主要参考：[修复或重装IE](https://learn.microsoft.com/zh-cn/troubleshoot/developer/browsers/installation/how-to-repair-or-reinstall-ie)\n\n对于Win10系统，由于无法运行IE的安装程序，因此需要用Windows的文件检查器来修复文件（事实上微软是提供了适配Win10的IE版本安装文件的），对着教程：[使用系统文件检查器工具](https://support.microsoft.com/zh-cn/topic/%E4%BD%BF%E7%94%A8%E7%B3%BB%E7%BB%9F%E6%96%87%E4%BB%B6%E6%A3%80%E6%9F%A5%E5%99%A8%E5%B7%A5%E5%85%B7%E4%BF%AE%E5%A4%8D%E4%B8%A2%E5%A4%B1%E6%88%96%E6%8D%9F%E5%9D%8F%E7%9A%84%E7%B3%BB%E7%BB%9F%E6%96%87%E4%BB%B6-79aa86cb-ca52-166a-92a3-966e85d4094e) 做一遍就行，完成后在功能中重启IE11并重启电脑，即可重新安装IE。\n');
INSERT INTO essays values(4, 1, 1, 1, NULL, 51285, 45503, "2023-12-05 21:00:00", "2023-12-05 21:00:00", 'GitHack 使用及 Python2 和 Python3 区分使用', 'GitHack 使用及 Python2 和 Python3 区分使用', "# GitHack解决python版本问题\n\n为了使用GitHack工具需要使用Py2，已有的py3运行会出现如下报错\n![](https://img2022.cnblogs.com/blog/2724756/202201/2724756-20220124002956105-513360279.png)\n重新安装Py2后发现cmd中py命令可以区分\n`py`—python3\n`python`—python2\n`py -2 xxx`—python2\n`py -3 xxx`—python3\n或者在python文件源码开头加入`# !python2`或`# !python3`\n但GitHack仍有如下报错：\n![](https://img2022.cnblogs.com/blog/2724756/202201/2724756-20220124003156143-1923138930.png)\n发现是python的运行反馈（Traceback），修改目标地址后报错出现变化：\n![](https://img2022.cnblogs.com/blog/2724756/202201/2724756-20220124003740671-1696467487.png)\n好吧。。。我觉得是我的打开方式(目标网址)出问题了。\n总之在windows上可以正常运行GitHack。\nGitHack使用格式:`GitHack.py http://xxxxx/.git/`\n\n**注意务必要在最后加上\".git/\"才能正常复原**\n————————\npy2的pip使用：`py -2 -m pip`或 `pip2`或`python -m pip`\npy3的pip使用：`py -3 -m pip`或 `pip3`或`py -m pip`\n\n**2022/5/6 更新**\n\n换了台机子重装了一下python2，一开始下载的官网2.7.12版本的python，安装时没有勾选将python添加到路径的选项，并采用手动安装setuptools的方法安装pip2，结果就是 `py -2` 访问不到Python2，只能修改exe文件为\"python2\"，用 `python2` 来访问，同时pip2也无法访问，会出现报错 `Fatal error in launcher: Unable to create process using \'\"\'` ，未能解决，只能使用 `python2 -m pip` 来访问pip2。\n\n于是将该python2卸载，重新下了一份2.7.15版本的python，在安装界面直接勾选添加到路径，安装完毕后确保python23的exe文件都未改名，即可通过 `py -n` 选择版本，同时使用 `python -2 -m pip install pip --force-reinstall` 先安装pip2，再用 `py -2 -m pip install --upgrade pip --force-reinstall` 更新pip2，最后就可以使用 `pip2` 来访问了。\n\n---\n\n### 最新更新\n\nPython 管理可以考虑用 Conda 或者 Pyenv 等众多工具。");
