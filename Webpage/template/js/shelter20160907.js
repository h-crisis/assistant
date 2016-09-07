/**
 * Created by manabu on 2016/05/19.
 */

var shelterLayer = new ol.layer.Tile({
    source: new ol.source.TileWMS({
        url: wms,
        params: {
            'VERSION': '1.1.1',
            tiled: true,
            STYLES: '',
            LAYERS: 'shelter'
        }
    })
});

function createShelterPopupHTML(feature) {
    var html = '';
    html = 'name: ' + feature.get('name');
    console.log('TEST: ' + feature.get('name'));
    return html;
}