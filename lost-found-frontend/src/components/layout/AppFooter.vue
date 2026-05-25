<template>
  <footer class="app-footer">
    <div class="footer-inner">
      <div class="footer-left">
        <span class="footer-icon">&#128269;</span>
        <span class="footer-brand">失物招领管理系统</span>
      </div>
      <div class="footer-center">
        <span class="footer-copy">&copy; 2026 Lost & Found System. All rights reserved.</span>
      </div>
      <div class="footer-right">
        <span class="footer-version">v1.0.0</span>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.app-footer {
  background: rgba(15, 23, 42, 0.95);
  backdrop-filter: blur(10px);
  border-top: 1px solid rgba(255, 255, 255, 0.05);
  margin-top: auto;
}
.footer-inner {
  max-width: 1400px;
  margin: 0 auto;
  padding: 16px 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.footer-left {
  display: flex;
  align-items: center;
  gap: 8px;
}
.footer-icon {
  font-size: 18px;
  filter: drop-shadow(0 0 6px rgba(99, 102, 241, 0.4));
}
.footer-brand {
  background: linear-gradient(135deg, #818cf8, #6ee7b7);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-size: 14px;
  font-weight: 600;
}
.footer-copy {
  color: var(--gray-500);
  font-size: 13px;
}
.footer-right {
  display: flex;
  align-items: center;
}
.footer-version {
  color: var(--gray-600);
  font-size: 12px;
  padding: 4px 10px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.08);
}

@media (max-width: 768px) {
  .footer-inner {
    flex-direction: column;
    gap: 8px;
    text-align: center;
  }
  .footer-right {
    display: none;
  }
}
</style>
