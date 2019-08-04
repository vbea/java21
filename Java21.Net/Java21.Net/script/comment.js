var img1, img2, img3, img4, img5;
var select;
var imgLight = "images/ic_star_light.png";
var imgNormal = "images/ic_star_normal.png";
function MouseOver(id) {
    img1 = get("imgStar1").getAttribute("src");
    img2 = get("imgStar2").getAttribute("src");
    img3 = get("imgStar3").getAttribute("src");
    img4 = get("imgStar4").getAttribute("src");
    img5 = get("imgStar5").getAttribute("src");
    switch (id) {
        case 1:
            get("imgStar1").setAttribute("src", imgLight);
            break;
        case 2:
            get("imgStar1").setAttribute("src", imgLight);
            get("imgStar2").setAttribute("src", imgLight);
            break;
        case 3:
            get("imgStar1").setAttribute("src", imgLight);
            get("imgStar2").setAttribute("src", imgLight);
            get("imgStar3").setAttribute("src", imgLight);
            break;
        case 4:
            get("imgStar1").setAttribute("src", imgLight);
            get("imgStar2").setAttribute("src", imgLight);
            get("imgStar3").setAttribute("src", imgLight);
            get("imgStar4").setAttribute("src", imgLight);
            break;
        case 5:
            get("imgStar1").setAttribute("src", imgLight);
            get("imgStar2").setAttribute("src", imgLight);
            get("imgStar3").setAttribute("src", imgLight);
            get("imgStar4").setAttribute("src", imgLight);
            get("imgStar5").setAttribute("src", imgLight);
            break;
    }
}

function MouseOut() {
    get("imgStar1").setAttribute("src", img1);
    get("imgStar2").setAttribute("src", img2);
    get("imgStar3").setAttribute("src", img3);
    get("imgStar4").setAttribute("src", img4);
    get("imgStar5").setAttribute("src", img5);
    switch (select) {
        case 1:
            get("imgStar1").setAttribute("src", imgLight);
            break;
        case 2:
            get("imgStar1").setAttribute("src", imgLight);
            get("imgStar2").setAttribute("src", imgLight);
            break;
        case 3:
            get("imgStar1").setAttribute("src", imgLight);
            get("imgStar2").setAttribute("src", imgLight);
            get("imgStar3").setAttribute("src", imgLight);
            break;
        case 4:
            get("imgStar1").setAttribute("src", imgLight);
            get("imgStar2").setAttribute("src", imgLight);
            get("imgStar3").setAttribute("src", imgLight);
            get("imgStar4").setAttribute("src", imgLight);
            break;
        case 5:
            get("imgStar1").setAttribute("src", imgLight);
            get("imgStar2").setAttribute("src", imgLight);
            get("imgStar3").setAttribute("src", imgLight);
            get("imgStar4").setAttribute("src", imgLight);
            get("imgStar5").setAttribute("src", imgLight);
            break;
    }
}

function imgClick(id) {
    img1 = img2 = img3 = img4 = img5 = imgNormal;
    if (select == id)
        select = 0
    else
        select = id;
    get("hidStart").value = select;
    get("labError").innerText = "";
    MouseOut();
}

function imgClear() {
    select = 0;
}

function check() {
    if (get("hidStart").value == "0") {
        get("labError").innerText = "请点击星星评分";
        return false;
    }
    if (get("txtComment").value.length == 0) {
        get("labError").innerText = "请输入评论内容";
        return false;
    }
    imgClear();
    return true;
}

function get(id) {
    return document.getElementById(id);
}

function show_content(str) {
    str = str.replace(/\</g, '&lt;');
    str = str.replace(/\>/g, '&gt;');
    str = str.replace(/\n/g, '<br/>');
    str = str.replace(/\[em_([0-9]*)\]/g, '<img src="images/arclist/$1.gif" border="0" />');
    return str;
}