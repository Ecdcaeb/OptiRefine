Tour-DiffAndPatch
==================
这是 [《diff 和 patch 的入门（及 Windows 下的用法）》](http://orzfly.com/html/diff-and-patch-and-windows.html) 一文中提到的适合 Windows 使用的 diff 和 patch。

文件清单
--------
<dl>
  <dt>README.md</dt>
  <dd>您正在阅读的这个说明文档。</dd>
  <dt>msys-1.0.dll</dt>
  <dd>MSYS POSIX Emulation DLL 1.0.12</dd>
  <dt>diff.exe</dt>
  <dd>diff - GNU diffutils version 2.7</dd>
  <dt>patch.exe</dt>
  <dd>patch 2.5</dd>
  <dt>patch.exe.manifest</dt>
  <dd>解决 patch.exe 的 UAC 问题所需的清单文件</dd>
</dl>

如何使用
--------
我个人推荐您将除了说明文件之外的四个文件都放置到 Windows\System32 目录下，
当然放到您 PATH 变量中所包含的目录也可以。

在这之后您便可以在命令行提示符中享受 diff 与 patch 给您带来的便利。