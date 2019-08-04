<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="DataList.aspx.cs" Inherits="Database_Tools.DataList" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Database Tools</title>
    <style type="text/css">
     html, body
     {
            width: 100%;
            height: 100%;
            margin: 0px;
            padding: 0px;
     }
     .data
     {
         min-width:30%;
         max-width:100%;
         height:100%;
         padding-left:5px;
     }
     .div{text-align:center;
          width:100%;
          float:left;
          background-color:White;
          position:fixed;
          _position:absolute;
          _top:expression(eval(documentElement.scrollTop));
          z-index:1000;
          left:0;
          top:0;
     }
     .top
     {
         background-color:#1299FF;
         color:White;
         position:relative;
         table-layout:fixed;
         z-index:10;
     }
     ul
     {
         position:absolute;
         bottom:0;
         text-align:center;
     }
     ul,li
     {
         padding:0;
         margin:0;
         list-style:none;
     }
    </style>
    <script src="../script/jquery-1.4.1.min.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#gvDataList tr").each(function (index) {
                if ($(this).children("td").length > 0) {
                    $(this).children("td").each(function (index1) {
                        if ($(this).text() == "<i style='color:#999'>NULL</i>")
                            $(this).html("<i style='color:#999'>NULL</i>");
                    });
                }
            });
        });
    </script>
</head>
<body>
    <form id="form1" runat="server">
    <div style="width:100%; height:100%; padding:0px; margin:0px; float:left;">
        <div class="div">
            <font size="+2">Database Tools v3.0</font>
            <br />
            <br />
            <div style="float:left; padding-left:10px"><asp:Literal ID="litName" runat="server"></asp:Literal>
                <asp:HiddenField ID="hidTableName" runat="server" /></div>
            <div style="float:right; padding-right:10px;"><asp:Literal ID="litPage" runat="server"></asp:Literal></div>
        </div>
        <div style="float:left; padding-top:2px;">
            <font size="+2">Database</font>
            <br />
            <br />
            <div style="float:left;">The data table not found.</div>
        </div>
        <div style="text-align:center; width:100%; height:100%; float:left;">
            <asp:Panel ID="panData" runat="server" Height="100%" Width="100%">
                <div style="padding-left:5px; float:left;">
                    <asp:GridView ID="gvDataList" runat="server" CellPadding="4" 
                        ForeColor="#333333" AllowPaging="True" 
                        onpageindexchanging="gvDataList_PageIndexChanging" PageSize="50" 
                        CssClass="data" BorderStyle="None">
                        <AlternatingRowStyle BackColor="White" ForeColor="#284775" />
                        <EditRowStyle BackColor="#999999" />
                        <EmptyDataTemplate>
                            <div style="text-align:center; width:100%">此表中没有数据！</div>
                        </EmptyDataTemplate>
                        <FooterStyle BackColor="#1299FF" Font-Bold="True" ForeColor="White" />
                        <HeaderStyle Font-Bold="True" CssClass="top"/>
                        <PagerSettings FirstPageText="首页" LastPageText="尾页" Mode="NumericFirstLast" 
                            NextPageText="下一页" PreviousPageText="上一页" />
                        <PagerStyle BackColor="#1299FF" ForeColor="White" HorizontalAlign="Center" />
                        <RowStyle BackColor="#F7F6F3" ForeColor="#333333" />
                        <SelectedRowStyle BackColor="#E2DED6" Font-Bold="True" ForeColor="#333333" />
                        <SortedAscendingCellStyle BackColor="#E9E7E2" />
                        <SortedAscendingHeaderStyle BackColor="#506C8C" />
                        <SortedDescendingCellStyle BackColor="#FFFDF8" />
                        <SortedDescendingHeaderStyle BackColor="#6F8DAE" />
                    </asp:GridView>
                    <br />
                </div>
            </asp:Panel>
        </div>
    </div>
    </form>
</body>
</html>
