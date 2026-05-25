<template>
  <footer class="app-footer">
    <div class="footer-inner">
      <span class="footer-brand">失物招领管理系统</span>
      <span class="footer-sep">&middot;</span>
      <span class="footer-copy">&copy; 2024 All rights reserved</span>
    </div>
  </footer>
</template>

<style scoped>
.app-footer {
  background: var(--gray-800);
  border-top: 1px solid var(--gray-700);
  margin-top: auto;
}
.footer-inner {
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}
.footer-brand {
  color: var(--gray-400);
  font-size: 13px;
  font-weight: 500;
}
.footer-sep {
  color: var(--gray-600);
}
.footer-copy {
  color: var(--gray-500);
  font-size: 13px;
}
</style>
