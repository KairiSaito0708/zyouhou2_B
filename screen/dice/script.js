const btn = document.getElementById('btn');
const allDice = document.querySelectorAll('.dice-strip');
let isMoving = false;

btn.addEventListener('click', function() {
  
  if (isMoving) {
    // --- 止める処理 ---
    // すべてのサイコロから .moving クラスを外す
    allDice.forEach(function(dice) {
      dice.classList.remove('moving');
    });
    
    btn.textContent = 'スタート';
    isMoving = false;

  } else {
    // --- 動かす処理 ---
    // すべてのサイコロに .moving クラスをつける
    allDice.forEach(function(dice) {
      dice.classList.add('moving');
    });

    btn.textContent = 'ストップ';
    isMoving = true;
  }
});