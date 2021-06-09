#### 新建分支
首先, 每次开发新功能, 都应该新建一个单独的分支(这方面可以参考 **Git分支管理策略**). 

```bash
# 获取主干最新代码
$ git checkout master
$ git pull

# 新建一个开发分支 myfeature
$ git checkout -b myfeature
```

#### 提交分支commit
分支修改之后, 就可以提交commit了
```bash
$ git add --all
$ git status
$ git commit --verbose
```

git add 命令的all参数, 表示保存所有变化（包括新建、修改和删除）. 从Git 2.0开始, all是 git add 的默认参数, 所以也可以用 git add . 代替. 

git status 命令, 用来查看发生变动的文件. 

git commit 命令的verbose参数, 会列出 diff 的结果. 


#### 提交信息书写
提交commit时，必须给出完整扼要的提交信息，下面是一个范本。

```bash
git commit -m yourMessage
```
这个提交的信息需要小于50个字符
