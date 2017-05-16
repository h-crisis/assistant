<?php

include('./php/mpdf60/mpdf.php');

$mpdf = new mPDF('sjis', 'A4');
$mpdf->WriteHTML('<a>Section 00<a>');
$mpdf->Output();

?>