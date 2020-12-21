<html>
    <head>
        <link rel="stylesheet" href="style.css" />
    </head>
    <body>
    <?php
        function endsWith($haystack, $needle)
        {
            $length = strlen($needle);
            if (!$length) {
                return true;
            }
            return substr($haystack, -$length) === $needle;
        }
        
        $fs = scandir("tiles");
        
        $fs = array_filter($fs, function($f) {
//            echo "$f";
            $endsWith = endsWith($f, ".png");
            return $endsWith;
        });
        
        $fs = array_values($fs);
        
        for ($y = 0; $y < 12; $y++) {
            echo "<div>";
            for ($x = 0; $x < 12; $x++) {
                $f = $fs[$x * $y];
                echo "<img draggable='true' src='tiles/$f' class='draggable'/>";
            }
            echo "</div>";
        } ?>
    
    <script>
        for (let draggable of document.getElementsByClassName("draggable")) {
            dragElement(draggable);
        }
        
        function dragElement(elmnt) {
            var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
            if (document.getElementById(elmnt.id + "header")) {
                // if present, the header is where you move the DIV from:
                document.getElementById(elmnt.id + "header").onmousedown = dragMouseDown;
            } else {
                // otherwise, move the DIV from anywhere inside the DIV:
                elmnt.onmousedown = dragMouseDown;
            }

            function dragMouseDown(e) {
                e = e || window.event;
                e.preventDefault();
                // get the mouse cursor position at startup:
                pos3 = e.clientX;
                pos4 = e.clientY;
                document.onmouseup = closeDragElement;
                // call a function whenever the cursor moves:
                document.onmousemove = elementDrag;
            }

            function elementDrag(e) {
                e = e || window.event;
                e.preventDefault();
                // calculate the new cursor position:
                pos1 = pos3 - e.clientX;
                pos2 = pos4 - e.clientY;
                pos3 = e.clientX;
                pos4 = e.clientY;
                // set the element's new position:
                elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
                elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
            }

            function closeDragElement() {
                // stop moving when mouse button is released:
                document.onmouseup = null;
                document.onmousemove = null;
            }
        }
        
        
    </script>
    
    </body>
</html>

