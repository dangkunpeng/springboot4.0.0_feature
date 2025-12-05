// 元素
    const canvas = document.getElementById('canvas');
    const ctx = canvas.getContext('2d');
    const batchInput = document.getElementById('batchSize');
    const singleBtn = document.getElementById('singleBtn');
    const startStopBtn = document.getElementById('startStopBtn');
    const clearBtn = document.getElementById('clearBtn');
    const totalEl = document.getElementById('total');
    const insideEl = document.getElementById('inside');
    const piEl = document.getElementById('pi');
    const elapsedEl = document.getElementById('elapsed');
    const rateEl = document.getElementById('rate');
    const pointSizeInput = document.getElementById('pointSize');

    // 统计
    let totalPoints = 0;
    let insidePoints = 0;
    let running = false;
    let startTime = 0;
    let rafId = null;

    // 圆心与半径（画布坐标）
    const W = canvas.width;
    const H = canvas.height;
    const cx = W / 2;
    const cy = H / 2;
    const r = Math.min(W, H) / 2;

    // 预画单位圆（边界）
    function drawCircle() {
      ctx.clearRect(0, 0, W, H);
      ctx.beginPath();
      ctx.arc(cx, cy, r, 0, Math.PI * 2);
      ctx.strokeStyle = '#000';
      ctx.lineWidth = 2;
      ctx.stroke();
    }

    drawCircle();

    // 将单位方形 (0..1,0..1) 点映射到画布中心化的坐标系并绘制点
    function plotPoint(u, v, inside, size) {
      // u,v in [0,1)
      // 映射到中心范围 [-r,r] 然后偏移到画布中心
      const x = cx + (u - 0.5) * 2 * r;
      const y = cy + (v - 0.5) * 2 * r;
      ctx.fillStyle = inside ? 'rgba(0,160,0,0.9)' : 'rgba(200,40,40,0.8)';
      if (size <= 1) {
        ctx.fillRect(Math.round(x), Math.round(y), 1, 1);
      } else {
        ctx.fillRect(x - size/2, y - size/2, size, size);
      }
    }

    // 生成批次点并更新统计
    function generateBatch(n) {
      const size = Math.max(1, Math.min(4, Number(pointSizeInput.value) || 1));
      for (let i = 0; i < n; i++) {
        const u = Math.random();
        const v = Math.random();
        // 计算距离到中心（单位圆半径 = 1/2 在映射后）
        // we can test inside by mapping to [-1,1] coordinates: dx = 2u-1, dy = 2v-1
        const dx = 2 * u - 1;
        const dy = 2 * v - 1;
        const inside = dx * dx + dy * dy <= 1;
        totalPoints++;
        if (inside) insidePoints++;
        plotPoint(u, v, inside, size);
      }
      updateStats();
    }

    // 更新统计显示
    function updateStats() {
      totalEl.textContent = totalPoints;
      insideEl.textContent = insidePoints;
      const piEstimate = totalPoints > 0 ? (4 * insidePoints / totalPoints) : 0;
      piEl.textContent = piEstimate.toFixed(6);
      const elapsed = startTime ? (Date.now() - startTime) : 0;
      elapsedEl.textContent = elapsed;
      const rate = elapsed > 0 ? Math.round(totalPoints / (elapsed / 1000)) : 0;
      rateEl.textContent = rate;
    }

    // 控制循环（使用 requestAnimationFrame 保持 UI 流畅）
    function start() {
      if (running) return;
      running = true;
      startStopBtn.textContent = '停止';
      if (totalPoints === 0) startTime = Date.now();
      function loop() {
        if (!running) return;
        const batch = Math.max(1, Math.floor(Number(batchInput.value) || 1000));
        generateBatch(batch);
        // 每帧生成一批，数值可调整
        rafId = requestAnimationFrame(loop);
      }
      rafId = requestAnimationFrame(loop);
    }

    function stop() {
      running = false;
      startStopBtn.textContent = '开始';
      if (rafId !== null) cancelAnimationFrame(rafId);
      rafId = null;
    }

    // 事件绑定
    singleBtn.addEventListener('click', () => {
      if (totalPoints === 0) startTime = Date.now();
      generateBatch(Math.max(1, Math.floor(Number(batchInput.value) || 1000)));
    });

    startStopBtn.addEventListener('click', () => {
      running ? stop() : start();
    });

    clearBtn.addEventListener('click', () => {
      stop();
      totalPoints = 0;
      insidePoints = 0;
      startTime = 0;
      drawCircle();
      updateStats();
    });

    // 在窗口缩放等情况下可重绘圆（此示例画布大小固定）
    window.addEventListener('resize', () => {
      drawCircle();
    });