/**
 * Created by komori on 2016/11/11.
 */

var count = 0;

function fade() {
    if (count%2 == 0) {
        document.getElementById("fadeLayer").style.display = 'block';
        document.getElementById("printSetting").style.display = 'block';
        count++;
    } else {
        document.getElementById("fadeLayer").style.display = 'none';
        document.getElementById("printSetting").style.display = 'none';
        count++
    }
}


var dims = {
    a0: [1189, 841],
    a1: [841, 594],
    a2: [594, 420],
    a3: [420, 297],
    a4: [297, 210],
    a5: [210, 148]
};

var loading = 0;
var loaded = 0;


var exportButton = document.getElementById('export-pdf');

exportButton.addEventListener('click', function() {

    exportButton.disabled = true;
    document.body.style.cursor = 'progress';

    var format = document.getElementById('format').value;
    var resolution = document.getElementById('resolution').value;
    var dim = dims[format];
    var width = Math.round(dim[0] * resolution / 25.4);
    var height = Math.round(dim[1] * resolution / 25.4);
    var size = /** @type {ol.Size} */ (map.getSize());
    var extent = map.getView().calculateExtent(size);

    var source = baseLayer.getSource();

    var tileLoadStart = function() {
        ++loading;
    };

    var tileLoadEnd = function() {
        ++loaded;
        if (loading === loaded) {
            var canvas = this;
            window.setTimeout(function() {
                loading = 0;
                loaded = 0;
                var data = canvas.toDataURL('image/png');
                var pdf = new jsPDF('landscape', undefined, format);
                pdf.addImage(data, 'JPEG', 0, 0, dim[0], dim[1]);
                pdf.save('map.pdf');
                source.un('tileloadstart', tileLoadStart);
                source.un('tileloadend', tileLoadEnd, canvas);
                source.un('tileloaderror', tileLoadEnd, canvas);
                map.setSize(size);
                map.getView().fit(extent, size);
                map.renderSync();
                exportButton.disabled = false;
                document.body.style.cursor = 'auto';
            }, 100);
        }
    };

    map.once('postcompose', function(event) {
        source.on('tileloadstart', tileLoadStart);
        source.on('tileloadend', tileLoadEnd, event.context.canvas);
        source.on('tileloaderror', tileLoadEnd, event.context.canvas);
    });

    map.setSize([width, height]);
    map.getView().fit(extent, /** @type {ol.Size} */ (map.getSize()));
    map.renderSync();

}, false);