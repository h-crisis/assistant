/**
 * Created by komori on 2017/05/23.
 */


//work directory
zip.workerScriptsPath = "../../js/";
//server address.(example)
var url = 'http://h-crisis.niph.go.jp/assistant/wp-content/uploads/sites/4/data/hcrisis_medical_status.geojson.zip';
//start zip read.

// create the blob object storing the data to compress
var blob = new Blob([  ], {
    type : "application/geojson"
});

function zipBlob(filename, blob, callback) {
    // use a zip.BlobWriter object to write zipped data into a Blob object
    zip.createWriter(new zip.BlobWriter("application/zip"), function(zipWriter) {
        // use a BlobReader object to read the data stored into blob variable
        zipWriter.add(filename, new zip.HttpReader(url), function(data) {
            // close the writer and calls callback function
            zipWriter.close(callback);
        });
    }, onerror);
}

function unzipBlob(blob, callback) {
    // use a zip.BlobReader object to read zipped data stored into blob variable
    zip.createReader(new zip.HttpReader(url), function(zipReader) {
        // get entries from the zip file
        zipReader.getEntries(function(entries) {
            // get data from the first file
            entries[0].getData(new zip.TextWriter(), function(data) {
                // close the reader and calls callback function with uncompressed data as parameter
                //console.log(data);
                zipReader.close();
                callback(data);
            });
        });
    }, onerror);
}

function onerror(message) {
    console.error(message);
}


