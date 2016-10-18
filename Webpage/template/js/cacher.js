/**
 * Created by komori on 2016/10/18.
 */

// navigator.serviceWorkerがある場合
if (navigator.serviceWorker) {

    // service-worker.jsをService Workerとして登録する
    navigator.serviceWorker.register('./service-worker.js', {
        scope: '.'
    }).then(function onFulfilled () {

        // service-worker.jsがひと通り評価され、インストールが成功した場合
        console.log('Service Worker was installed.');
    }, function onRejected () {

        // service-worker.jsのインストールが失敗した場合
        console.log('Service Worker was not installed.');
    });
}