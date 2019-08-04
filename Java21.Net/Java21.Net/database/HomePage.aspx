<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="HomePage.aspx.cs" Inherits="Database_Tools.HomePage"
    MaintainScrollPositionOnPostback="true" ValidateRequest="false" %>

<!DOCTYPE />
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Database Tools</title>
    <link href="../css/home.css" rel="stylesheet" />
    <script type="text/javascript" src="../script/jquery-1.4.1.min.js"></script>
</head>
<body>
    <form id="form1" runat="server">
    <asp:ScriptManager ID="ajaxMng" runat="server" AsyncPostBackTimeout="0">
    </asp:ScriptManager>
    <div style="width: 100%; height: 100%; padding: 0px; margin: 0px;">
        <asp:UpdatePanel ID="ajaxPan" runat="server">
            <ContentTemplate>
                <div class="top_div">
                    <font size="+2">Database Tools v3.0</font>
                    <br />
                    <br />
                    <br />
                    <div style="width: 100%; max-height: 5%; padding-left: 1px; text-align: left; float: left;">
                        <div style="float: left; padding-left: 5%">
                            &nbsp;&nbsp; 数据库：<asp:DropDownList ID="ddlDatabase" runat="server" AutoPostBack="True"
                                OnSelectedIndexChanged="ddlDatabase_SelectedIndexChanged">
                                <asp:ListItem>请选择...</asp:ListItem>
                            </asp:DropDownList>
                            （<asp:CheckBox ID="chkSystemDatabase" runat="server" AutoPostBack="True" Checked="True"
                                OnCheckedChanged="chkSystemDatabase_CheckedChanged" Text="显示系统数据库" />）
                            <asp:LinkButton ID="btnRefresh" runat="server" onclick="btnRefresh_Click">刷新</asp:LinkButton>
                            &nbsp;<asp:LinkButton ID="btnSetting" runat="server" OnClick="btnSetting_Click">设置</asp:LinkButton>
                        </div>
                        <div style="padding-right: 6%; float: right;">
                            <asp:Label ID="labHeaderName" runat="server" ForeColor="#1299FF"></asp:Label>
                            <asp:HyperLink ID="linkExec" runat="server" NavigateUrl="~/database/ExecuteSQL.aspx"
                                Target="_blank">转到执行>></asp:HyperLink>
                        </div>
                    </div>
                    <div style="width: 100%; padding-left: 1px; text-align: center; float: left;">
                        <hr style="width: 90%" />
                    </div>
                </div>
                <!--正文开始-->
                <div style="width: 100%; height: 100%; padding: 0px; margin: 0px; background-color: #FEFEFE">
                    <div style="width: 100%; height: 80%; position: absolute; top: 18%; bottom: 0px;
                        left: 0px; right: 0px; z-index: 100">
                        <div style="width: 20%; float: left; height: 100%; border: 1px solid #999; margin-left: 7%;
                            z-index: 999;">
                            <asp:Panel ID="panToolBar" runat="server" Visible="false" Height="4%">
                                <div style="width: 100%; height: 95%; border-bottom: 1px solid #999;">
                                    <table style="width: 100%; text-align: center;">
                                        <tr>
                                            <td style="width: 30%;">
                                                <asp:LinkButton ID="lnkRefresh" runat="server" Text="刷新" OnClick="ddlDatabase_SelectedIndexChanged"></asp:LinkButton>
                                            </td>
                                            <td style="width: 30%;">
                                                <asp:LinkButton ID="lnkDelete" runat="server" Text="删除" Visible="false" OnClick="lnkDelete_Click"></asp:LinkButton>
                                            </td>
                                            <td style="width: 30%;">
                                                <asp:LinkButton ID="lnkNewTable" runat="server" Text="新建" OnClick="lnkNewTable_Click"></asp:LinkButton>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </asp:Panel>
                            <div style="width: 100%; height: 95.5%;">
                                <asp:Panel ID="panTabControl" runat="server" Visible="false" CssClass="pan_tab">
                                    <asp:MultiView ID="mlvList" runat="server">
                                        <asp:View ID="viewTable" runat="server">
                                            <asp:ListBox ID="lsbDataTable" runat="server" CssClass="tablist" AutoPostBack="True"
                                                OnSelectedIndexChanged="lsbDataTable_SelectedIndexChanged" EnableTheming="True">
                                            </asp:ListBox>
                                        </asp:View>
                                        <asp:View ID="viewView" runat="server">
                                            <asp:ListBox ID="lsbDataView" runat="server" CssClass="tablist" AutoPostBack="True"
                                                EnableTheming="True" OnSelectedIndexChanged="lsbDataView_SelectedIndexChanged">
                                            </asp:ListBox>
                                        </asp:View>
                                        <asp:View ID="viewProcedure" runat="server">
                                            <asp:ListBox ID="lsbProcedures" runat="server" CssClass="tablist" AutoPostBack="True"
                                                EnableTheming="True" OnSelectedIndexChanged="lsbProcedures_SelectedIndexChanged">
                                            </asp:ListBox>
                                        </asp:View>
                                    </asp:MultiView>
                                    <div class="div_tab">
                                        <asp:Button ID="btnSelectTable" runat="server" Text="Table" CssClass="btn_tab_pause"
                                            OnClick="btnSelectTable_Click" ToolTip="表" /><asp:Button ID="btnSelectView" runat="server"
                                                Text="View" CssClass="btn_tab" OnClick="btnSelectView_Click" ToolTip="视图" /><asp:Button
                                                    ID="btnSelectProcedures" runat="server" Text="StoredProcedure" CssClass="btn_tab"
                                                    OnClick="btnSelectProcedures_Click" ToolTip="存储过程" />
                                        <asp:HiddenField ID="hidTabType" runat="server" />
                                    </div>
                                </asp:Panel>
                            </div>
                        </div>
                        <div style="width: 65%; float: left; border: 1px solid #999; height: 100%; margin-left: 10px;">
                            <div style="width: 100%; height: 5%">
                                <asp:Panel ID="panMessage" runat="server" Visible="False">
                                    <div style="padding-bottom: 5px;">
                                        <div style="float: left; padding-top: 2px; padding-right: 10px;">
                                            &nbsp;<asp:Label ID="labTableName" runat="server" Style="width: 100%; word-break: break-all;"
                                                Width="100%"></asp:Label><asp:HiddenField ID="hidSchemasName" runat="server" />
                                            <asp:HiddenField ID="hidTablesName" runat="server" />
                                            <br />
                                            <asp:Panel ID="panTableCount" runat="server">
                                                <font color="#999999" size="-1">表中共有数据：<asp:Literal ID="litDetail" runat="server">0</asp:Literal>
                                                    条</font></asp:Panel>
                                        </div>
                                        <div style="text-align: center; margin-right: 5px; float: right; width: auto; height: 30px;
                                            line-height: 30px; margin-top: 5px; background-color: #1299FF">
                                            <asp:HyperLink ID="btnShowTop100" NavigateUrl="~/database/DataList.aspx" Target="_blank" runat="server"
                                                Text="显示前1000条数据" CssClass="btn_link">显示前1000条数据</asp:HyperLink>
                                            <asp:Button ID="btnNewSelect" runat="server" Text="查询" BorderStyle="None" CssClass="btn"
                                                BorderWidth="0px" OnClick="btnNewSelect_Click"></asp:Button>
                                            <asp:Button ID="btnNewData" runat="server" Text="新增" CssClass="btn" BorderStyle="None"
                                                BorderWidth="0px" OnClick="btnNewData_Click"></asp:Button>
                                            <asp:Button ID="btnUpdate" runat="server" Text="修改" CssClass="btn" BorderStyle="None"
                                                BorderWidth="0px" OnClick="btnUpdate_Click"></asp:Button>
                                            <asp:Button ID="btnDelete" runat="server" Text="删除" CssClass="btn" BorderStyle="None"
                                                BorderWidth="0px" OnClick="btnUpdate_Click"></asp:Button>
                                            <asp:Button ID="btnDefine" runat="server" Text="定义"  CssClass="btn" BorderStyle="None"
                                                BorderWidth="0px" onclick="btnDefine_Click"/>
                                            <asp:Button ID="btnProcparam" runat="server" Text="参数" CssClass="btn" BorderStyle="None"
                                                BorderWidth="0px" OnClick="btnProcparam_Click"></asp:Button>
                                            <asp:Button ID="btnProcAction" runat="server" Text="执行存储过程" CssClass="btn" BorderStyle="None"
                                                BorderWidth="0px" OnClick="btnProcAction_Click"></asp:Button>
                                            <br />
                                        </div>
                                    </div>
                                </asp:Panel>
                                <asp:Timer ID="Timer1" runat="server" Enabled="False" Interval="2000" OnTick="Timer1_Tick">
                                </asp:Timer>
                            </div>
                            <div style="width: 100%; height: 94%;">
                                <asp:Panel ID="panSelect0" runat="server" Visible="false" Width="100%" BorderStyle="None">
                                    <div style="margin-top: 5px; padding-top: 5px; padding-bottom: 5px; width: 98%; float:left;">
                                        字段：<asp:DropDownList ID="ddlfield" runat="server">
                                            <asp:ListItem Text="请选择"></asp:ListItem>
                                        </asp:DropDownList>
                                        条件：<asp:TextBox ID="txtWhere" runat="server" onkeydown="if(event.keyCode==13) {document.all.btnAction.focus(); document.all.btnAction.click();}"></asp:TextBox>
                                        <asp:CheckBox ID="chkLike" runat="server" Text="模糊查询" />
                                        <asp:Button ID="btnAction" runat="server" Text="查询" OnClick="btnAction_Click" TabIndex="1" />
                                        <asp:Button ID="btnClose" runat="server" Text="关闭" OnClick="btnClose_Click" />
                                        <asp:Label ID="labTips" runat="server" CssClass="lisr"></asp:Label>
                                    </div>
                                </asp:Panel>
                                <asp:Panel ID="panSelect1" runat="server" Visible="false" ScrollBars="Auto" Width="100%"
                                    BorderStyle="None" CssClass="pan_mini" Height="94.5%">
                                    <div style="width: 100%; padding: 0px; max-height: 90%">
                                        <asp:GridView ID="gdSelectView" runat="server" CellPadding="4" ForeColor="#333333">
                                            <AlternatingRowStyle BackColor="White" ForeColor="#284775" />
                                            <EditRowStyle BackColor="#999999" />
                                            <EmptyDataRowStyle Font-Italic="False" />
                                            <EmptyDataTemplate>
                                                <div style="text-align: center; width: 100%">
                                                    没有查询到数据</div>
                                            </EmptyDataTemplate>
                                            <FooterStyle BackColor="#5D7B9D" Font-Bold="True" ForeColor="White" />
                                            <HeaderStyle BackColor="#1299FF" Font-Bold="True" ForeColor="White" />
                                            <PagerStyle BackColor="#284775" ForeColor="White" HorizontalAlign="Center" />
                                            <RowStyle BackColor="#F7F6F3" ForeColor="#333333" />
                                            <SelectedRowStyle BackColor="#E2DED6" Font-Bold="True" ForeColor="#333333" />
                                            <SortedAscendingCellStyle BackColor="#E9E7E2" />
                                            <SortedAscendingHeaderStyle BackColor="#506C8C" />
                                            <SortedDescendingCellStyle BackColor="#FFFDF8" />
                                            <SortedDescendingHeaderStyle BackColor="#6F8DAE" />
                                        </asp:GridView>
                                    </div>
                                </asp:Panel>
                                <asp:Panel ID="panUpdate" runat="server" Visible="false" ScrollBars="Auto" Width="100%"
                                    BorderStyle="None" CssClass="pan">
                                    <div style="padding: 0px; margin-top: 5px; padding-top: 5px;">
                                        <div style="float: left;">
                                            选择数据：字段：<asp:DropDownList ID="ddlUpdateFiled" runat="server" OnSelectedIndexChanged="ddlUpdateFiled_SelectedIndexChanged"
                                                AutoPostBack="True">
                                                <asp:ListItem Text="请选择"></asp:ListItem>
                                            </asp:DropDownList>
                                            条件：<asp:TextBox ID="txtUpdateWhere" runat="server" Width="128px" onkeydown="if(event.keyCode==13) {document.all.btnUSelect.focus(); document.all.btnUSelect.click();}"></asp:TextBox>
                                            <asp:Button ID="btnUSelect" runat="server" Text="查询" OnClick="btnUSelect_Click" />
                                            <asp:Button ID="btnDClose" runat="server" Text="关闭" OnClick="btnClose_Click" />
                                            <asp:Label ID="labUpdate" runat="server" CssClass="lisr"></asp:Label>
                                            <asp:Label ID="txtUpdateHWhere" runat="server" Text="" Visible="false"></asp:Label>
                                        </div>
                                        <asp:GridView ID="gdUpdateView" runat="server" CellPadding="4" ForeColor="#333333"
                                            AutoGenerateColumns="False" Width="100%" onkeydown="if(event.keyCode==13) {return false;}"
                                            OnDataBound="gdUpdateView_DataBound">
                                            <AlternatingRowStyle BackColor="White" ForeColor="#284775" />
                                            <Columns>
                                                <asp:TemplateField>
                                                    <HeaderTemplate>
                                                        <table style="width: 100%;">
                                                            <tr style="width: 100%;">
                                                                <td style="width: 25%; text-align: center;" class="grid">
                                                                    字段名
                                                                </td>
                                                                <td style="width: 16%; text-align: center;" class="grid">
                                                                    类型
                                                                </td>
                                                                <td style="width: 16%; text-align: center;" class="grid">
                                                                    允许为空
                                                                </td>
                                                                <td style="width: 35%; text-align: center;" class="grid">
                                                                    修改对应类型的值
                                                                </td>
                                                                <td style="width: 8%; text-align: center;">
                                                                    操作
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </HeaderTemplate>
                                                    <ItemTemplate>
                                                        <table style="width: 100%;">
                                                            <tr style="width: 100%;">
                                                                <td style="width: 25%; text-align: center;" class="grid">
                                                                    <asp:Label ID="labUpdateColumn" runat="server" Text='<%# Eval("column_name")%>'></asp:Label>
                                                                </td>
                                                                <td style="width: 16%; text-align: center;" class="grid">
                                                                    <%# Eval("data_type")%>
                                                                </td>
                                                                <td style="width: 16%; text-align: center;" class="grid">
                                                                    <%# Eval("is_null")%>
                                                                </td>
                                                                <td style="width: 35%; text-align: center;" class="grid">
                                                                    <asp:TextBox ID="txtDataUpdateValue" runat="server" CssClass="data_text" MaxLength='<%# Eval("max_length") %>'
                                                                        Text='<%# Eval("ins_value") %>' Enabled='<%# showState(Eval("is_identity")) %>'
                                                                        ClientIDMode="Static" onkeydown="if(event.keyCode==13) {return false;}" />
                                                                </td>
                                                                <td style="width: 8%; text-align: center;">
                                                                    <asp:LinkButton ID="linkRejectColumn" runat="server" Text="排除" CommandName='<%# getContinuousID()%>'
                                                                        OnClick="linkRejectColumn_Click"></asp:LinkButton>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </ItemTemplate>
                                                </asp:TemplateField>
                                            </Columns>
                                            <EditRowStyle BackColor="#999999" />
                                            <EmptyDataRowStyle Font-Italic="False" />
                                            <EmptyDataTemplate>
                                                <div style="text-align: center; width: 100%">
                                                    没有数据</div>
                                            </EmptyDataTemplate>
                                            <FooterStyle BackColor="#5D7B9D" Font-Bold="True" ForeColor="White" />
                                            <HeaderStyle BackColor="#1299FF" Font-Bold="True" ForeColor="White" />
                                            <PagerStyle BackColor="#284775" ForeColor="White" HorizontalAlign="Center" />
                                            <RowStyle BackColor="#F7F6F3" ForeColor="#333333" />
                                            <SelectedRowStyle BackColor="#E2DED6" Font-Bold="True" ForeColor="#333333" />
                                            <SortedAscendingCellStyle BackColor="#E9E7E2" />
                                            <SortedAscendingHeaderStyle BackColor="#506C8C" />
                                            <SortedDescendingCellStyle BackColor="#FFFDF8" />
                                            <SortedDescendingHeaderStyle BackColor="#6F8DAE" />
                                        </asp:GridView>
                                        <asp:Panel ID="panUpdateButton" runat="server" Height="32px" BackColor="#1299FF"
                                            Style="text-align: center; padding-bottom: 2px;">
                                            <div style="text-align: left; height: 30px; line-height: 30px; width: 33%; font-size: 13px;
                                                float: left;">
                                                <asp:Label ID="litUpdateTips" runat="server" Text="无法显示" ForeColor="White"></asp:Label>
                                            </div>
                                            <div style="text-align: center; height: 30px; width: 33%; line-height: 30px; float: left;">
                                                <asp:Button ID="btnDDelete" runat="server" Text="删除" OnClick="btnDDelete_Click" OnClientClick="return confirm('确定要删除该条数据吗？（删除后将无法恢复）')"
                                                    CssClass="btn_auto" BorderStyle="None" BorderWidth="0px" Height="30px" />
                                                &nbsp;<asp:Button ID="btnDUpdate" runat="server" Text="修改" OnClick="btnDUpdate_Click"
                                                    CssClass="btn_auto" OnClientClick="return confirm('确定要更新该数据吗？')" BorderStyle="None"
                                                    BorderWidth="0px" Height="30px" />
                                            </div>
                                        </asp:Panel>
                                    </div>
                                </asp:Panel>
                                <asp:Panel ID="panInsert" runat="server" Visible="false" ScrollBars="Auto" Width="100%"
                                    BorderStyle="None" CssClass="pan">
                                    <div style="width: 100%; padding: 0px; margin-top: 5px; padding-top: 5px;">
                                        <div style="float: left;">
                                            新增数据：
                                            <asp:CheckBox ID="chkUpdateEmpty" runat="server" Text="成功后清空输入框" />
                                            <asp:Label ID="litInsertTip" runat="server" CssClass="lisr"></asp:Label>
                                        </div>
                                        <div style="float: right;">
                                            <asp:Button ID="btnInsertAction" runat="server" Text="新增" OnClick="btnInsertAction_Click" />
                                            <asp:Button ID="btnPClose" runat="server" Text="关闭" OnClick="btnClose_Click" /></div>
                                        <asp:GridView ID="gdInsertView" runat="server" CellPadding="4" ForeColor="#333333"
                                            AutoGenerateColumns="False" Width="100%" onkeydown="if(event.keyCode==13) {return false;}"
                                            OnDataBound="gdInsertView_DataBound">
                                            <AlternatingRowStyle BackColor="White" ForeColor="#284775" />
                                            <Columns>
                                                <asp:TemplateField>
                                                    <HeaderTemplate>
                                                        <table style="width: 100%;">
                                                            <tr style="width: 100%;">
                                                                <td style="width: 25%; text-align: center;" class="grid">
                                                                    字段名
                                                                </td>
                                                                <td style="width: 16%; text-align: center;" class="grid">
                                                                    类型
                                                                </td>
                                                                <td style="width: 16%; text-align: center;" class="grid">
                                                                    允许为空
                                                                </td>
                                                                <td style="width: 35%; text-align: center;" class="grid">
                                                                    请输入对应类型的值
                                                                </td>
                                                                <td style="width: 8%; text-align: center;">
                                                                    操作
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </HeaderTemplate>
                                                    <ItemTemplate>
                                                        <table style="width: 100%;">
                                                            <tr style="width: 100%;">
                                                                <td style="width: 25%; text-align: center;" class="grid">
                                                                    <asp:Label ID="labInsertColumn" runat="server" Text='<%# Eval("column_name")%>'></asp:Label>
                                                                </td>
                                                                <td style="width: 16%; text-align: center;" class="grid">
                                                                    <%# Eval("data_type")%>
                                                                </td>
                                                                <td style="width: 16%; text-align: center;" class="grid">
                                                                    <asp:Label ID="labInsertColumnIsNull" runat="server" Text='<%# Eval("is_null")%>'></asp:Label>
                                                                </td>
                                                                <td style="width: 35%; text-align: center;" class="grid">
                                                                    <asp:TextBox ID="txtDataValue" runat="server" MaxLength='<%# Eval("max_length") %>'
                                                                        ClientIDMode="Inherit" CssClass="data_text" onkeydown="if(event.keyCode==13) {return false;}"></asp:TextBox>
                                                                </td>
                                                                <td style="width: 8%; text-align: center;">
                                                                    <asp:LinkButton ID="linkInsertRejectColumn" runat="server" Text="排除" CommandName='<%# getContinuousID()%>'
                                                                        OnClick="linkInsertRejectColumn_Click"></asp:LinkButton>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </ItemTemplate>
                                                </asp:TemplateField>
                                            </Columns>
                                            <EditRowStyle BackColor="#999999" />
                                            <EmptyDataTemplate>
                                                <div style="text-align: center; width: 100%">
                                                    没有数据</div>
                                            </EmptyDataTemplate>
                                            <FooterStyle BackColor="#5D7B9D" Font-Bold="True" ForeColor="White" />
                                            <HeaderStyle BackColor="#1299FF" Font-Bold="True" ForeColor="White" />
                                            <PagerStyle BackColor="#284775" ForeColor="White" HorizontalAlign="Center" />
                                            <RowStyle BackColor="#F7F6F3" ForeColor="#333333" />
                                            <SelectedRowStyle BackColor="#E2DED6" Font-Bold="True" ForeColor="#333333" />
                                            <SortedAscendingCellStyle BackColor="#E9E7E2" />
                                            <SortedAscendingHeaderStyle BackColor="#506C8C" />
                                            <SortedDescendingCellStyle BackColor="#FFFDF8" />
                                            <SortedDescendingHeaderStyle BackColor="#6F8DAE" />
                                        </asp:GridView>
                                    </div>
                                </asp:Panel>
                                <asp:Panel ID="panProcDefine" runat="server" CssClass="pan" Visible="False" ScrollBars="None">
                                     <asp:TextBox ID="txtProcDefine" runat="server" TextMode="MultiLine" style="min-height:580px; border:0; float:left; width:100%; max-height:97%; background-color:White; margin-top: 5px; padding-top: 5px;"></asp:TextBox>
                                </asp:Panel>
                                <asp:Panel ID="panProcParam" runat="server" CssClass="pan" Visible="False" ScrollBars="Auto">
                                    <div style="width: 100%; padding: 0px; margin-top: 5px; padding-top: 5px;">
                                        &nbsp;参数列表：
                                        <asp:GridView ID="gdProcpara" runat="server" CellPadding="4" ForeColor="#333333"
                                            AutoGenerateColumns="False" Width="100%">
                                            <AlternatingRowStyle BackColor="White" ForeColor="#284775" />
                                            <Columns>
                                                <asp:TemplateField>
                                                    <HeaderTemplate>
                                                        <table style="width: 100%;">
                                                            <tr style="width: 100%;">
                                                                <td style="width: 20%; text-align: center;" class="grid">
                                                                    参数名
                                                                </td>
                                                                <td style="width: 20%; text-align: center;" class="grid">
                                                                    数据类型
                                                                </td>
                                                                <td style="width: 20%; text-align: center;" class="grid">
                                                                    最大长度
                                                                </td>
                                                                <td style="width: 20%; text-align: center;" class="grid">
                                                                    参数类型
                                                                </td>
                                                                <td style="width: 20%; text-align: center;">
                                                                    字符长度
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </HeaderTemplate>
                                                    <ItemTemplate>
                                                        <table style="width: 100%;">
                                                            <tr style="width: 100%;">
                                                                <td style="width: 20%; text-align: center;" class="grid">
                                                                    <asp:Label ID="labInsertColumn" runat="server" Text='<%# Eval("ParaName")%>'></asp:Label>
                                                                </td>
                                                                <td style="width: 20%; text-align: center;" class="grid">
                                                                    <%# Eval("DataType")%>
                                                                </td>
                                                                <td style="width: 20%; text-align: center;" class="grid">
                                                                    <%# Eval("maxLength")%>
                                                                </td>
                                                                <td style="width: 20%; text-align: center;" class="grid">
                                                                    <%# Eval("Mode") %>
                                                                </td>
                                                                <td style="width: 20%; text-align: center;">
                                                                    <%# Eval("Character")%>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </ItemTemplate>
                                                </asp:TemplateField>
                                            </Columns>
                                            <EditRowStyle BackColor="#999999" />
                                            <EmptyDataTemplate>
                                                <div style="text-align: center; width: 100%">
                                                    该存储过程没有参数</div>
                                            </EmptyDataTemplate>
                                            <FooterStyle BackColor="#5D7B9D" Font-Bold="True" ForeColor="White" />
                                            <HeaderStyle BackColor="#1299FF" Font-Bold="True" ForeColor="White" />
                                            <PagerStyle BackColor="#284775" ForeColor="White" HorizontalAlign="Center" />
                                            <RowStyle BackColor="#F7F6F3" ForeColor="#333333" />
                                            <SelectedRowStyle BackColor="#E2DED6" Font-Bold="True" ForeColor="#333333" />
                                            <SortedAscendingCellStyle BackColor="#E9E7E2" />
                                            <SortedAscendingHeaderStyle BackColor="#506C8C" />
                                            <SortedDescendingCellStyle BackColor="#FFFDF8" />
                                            <SortedDescendingHeaderStyle BackColor="#6F8DAE" />
                                        </asp:GridView>
                                        <asp:Button ID="btnProcexecute" runat="server" Text="执行该存储过程" Style="float: right;
                                            margin-top: 5px; margin-right: 5px;" OnClick="btnProcexecute_Click" />
                                    </div>
                                </asp:Panel>
                                <asp:Panel ID="panProcExec" runat="server" CssClass="pan" Visible="False" Height="10%">
                                    <div style="width: 100%; padding-top: 5px; margin-top: 5px; padding-left: 5px;">
                                        <asp:TextBox ID="txtProcExec" runat="server" Height="32px" TextMode="MultiLine" Width="98%"></asp:TextBox><br />
                                        <div style="float: left; width: 98%; padding-top: 2px;">
                                            <div style="float: left; width: 70%; padding-top: 2px;">
                                                仅需传入参数值，每个参数之间用逗号分隔，不支持输出型参数的显示
                                                <asp:HiddenField ID="hidProcExec" runat="server" />
                                            </div>
                                            <div style="text-align: right; float: left; width: 28%">
                                                <asp:Button ID="brtnProcEClose" runat="server" Text="关闭" OnClick="btnClose_Click" />
                                                <asp:Button ID="btnProcExecAction" runat="server" Text="执行" OnClick="btnProcExecAction_Click" />
                                            </div>
                                        </div><br />
                                        <asp:Label ID="labProcExec" runat="server" Style="width: 98%; float:left; color: Red;"></asp:Label>
                                    </div>
                                </asp:Panel>
                                <asp:Panel ID="panProcExecData" runat="server" CssClass="pan_mini" Visible="False" ScrollBars="Auto">
                                    <div style="width: 100%; max-height: 90%">
                                        <asp:GridView ID="gdProcedureData" runat="server" CellPadding="4" ForeColor="#333333"
                                            Width="99.5%">
                                            <AlternatingRowStyle BackColor="White" ForeColor="#284775" />
                                            <EditRowStyle BackColor="#999999" />
                                            <EmptyDataRowStyle Font-Italic="False" />
                                            <EmptyDataTemplate>
                                                <div style="text-align: center; width: 100%">
                                                    执行成功，未查询到数据</div>
                                            </EmptyDataTemplate>
                                            <FooterStyle BackColor="#5D7B9D" Font-Bold="True" ForeColor="White" />
                                            <HeaderStyle BackColor="#1299FF" Font-Bold="True" ForeColor="White" />
                                            <PagerStyle BackColor="#284775" ForeColor="White" HorizontalAlign="Center" />
                                            <RowStyle BackColor="#F7F6F3" ForeColor="#333333" />
                                            <SelectedRowStyle BackColor="#E2DED6" Font-Bold="True" ForeColor="#333333" />
                                            <SortedAscendingCellStyle BackColor="#E9E7E2" />
                                            <SortedAscendingHeaderStyle BackColor="#506C8C" />
                                            <SortedDescendingCellStyle BackColor="#FFFDF8" />
                                            <SortedDescendingHeaderStyle BackColor="#6F8DAE" />
                                        </asp:GridView>
                                    </div>
                                </asp:Panel>
                                <asp:Panel ID="panNewTable" runat="server" CssClass="pan" Visible="False">
                                    <div style="width: 100%; padding: 0px; margin-top: 5px; padding-top: 5px;">
                                        <div style="float: left;">
                                            新增：
                                            <asp:RadioButton ID="rdbNewTable" runat="server" Text="表" Checked="True" GroupName="NewType"
                                                AutoPostBack="True" OnCheckedChanged="rdbNewTable_CheckedChanged" />
                                            <asp:RadioButton ID="rdbNewView" runat="server" GroupName="NewType" Text="视图" AutoPostBack="True"
                                                OnCheckedChanged="rdbNewTable_CheckedChanged" />
                                            <asp:RadioButton ID="rdbNewProc" runat="server" Text="存储过程" GroupName="NewType" AutoPostBack="True"
                                                OnCheckedChanged="rdbNewTable_CheckedChanged" />
                                            <asp:RadioButton ID="rdbCustom" runat="server" Text="自定义" GroupName="NewType" AutoPostBack="True"
                                                OnCheckedChanged="rdbNewTable_CheckedChanged" />
                                        </div>
                                        <div style="float: left; width: 100%; margin-top: 5px; padding-left: 5px;">
                                            <table class="table_cell">
                                                <tr>
                                                    <td style="width: 20%; text-align: center;">
                                                        <asp:Label ID="labNameTable_Name" runat="server" Text="表名："></asp:Label>
                                                    </td>
                                                    <td>
                                                        <asp:TextBox ID="txtNewTable_Name" runat="server" Width="98%"></asp:TextBox>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="width: 20%; text-align: center;">
                                                        <asp:Label ID="labColumnList" runat="server" Text="列定义：<br />（每一列之间用逗号分隔）"></asp:Label>
                                                    </td>
                                                    <td>
                                                        <asp:TextBox ID="txtColumnList" runat="server" Width="98%" Height="200px" TextMode="MultiLine"></asp:TextBox>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td colspan="2">
                                                        <asp:Button ID="btnNewTableClose" runat="server" Text="关闭" OnClick="btnNewTableClose_Click" />
                                                        &nbsp;<asp:Button ID="btnNewTable" runat="server" Text="执行" OnClick="btnNewTable_Click"
                                                            Width="65px" />&nbsp;
                                                        <asp:Label ID="labNewTableTip" runat="server" ForeColor="Red"></asp:Label>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                </asp:Panel>
                                <asp:Panel ID="panSettings" runat="server" CssClass="pan" Visible="False">
                                    <div style="width: 100%; padding: 0px; margin-top: 5px; padding-top: 5px;">
                                        <div style="float: left; width: 100%; margin-top: 5px; padding-left: 5px;">
                                            设置：<br />
                                            连接服务器：<asp:Label ID="labServer" runat="server" Text=""></asp:Label>
                                            &nbsp;<asp:LinkButton ID="linkExit" runat="server" Text="退出" OnClick="linkExit_Click"
                                                OnClientClick="return confirm('确定要退出当前数据库连接？')" ToolTip="退出"></asp:LinkButton>
                                            <asp:LinkButton ID="linkBack" runat="server" Text="返回" OnClick="linkExit_Click" ToolTip="返回"></asp:LinkButton>
                                            <br />
                                            当前数据库：<asp:Label ID="labDataBase" runat="server" Text=""></asp:Label>
                                            &nbsp;<asp:LinkButton ID="lnkDeleteDatabase" runat="server" 
                                                onclick="lnkDeleteDatabase_Click" OnClientClick="return confirm('确定要删除该数据库吗？删除后无法恢复');">删除</asp:LinkButton>
                                            <br />
                                            连接字符串：<asp:TextBox ID="txtConnection" runat="server" Style="max-width: 80%; width: 50%;"></asp:TextBox><br />
                                            <br />
                                            <div style="float:left; width:100%;">
                                                <asp:Button ID="btnReConnect" runat="server" Text="重新连接" OnClick="btnReConnect_Click" />
                                                &nbsp;<asp:Image ID="imgReConnect" runat="server" ImageUrl="~/images/loading.gif" 
                                                    Visible="False" /><asp:Label ID="labReConnect"
                                                runat="server" style="padding-left:1px; color:Red;"></asp:Label>
                                                <asp:Timer ID="Timer2" runat="server" Enabled="False" Interval="1000" 
                                                    ontick="Timer2_Tick"></asp:Timer>
                                            </div>
                                        </div>
                                        <div style="float: left; width: 100%; margin-top: 5px; padding-left: 5px;">
                                            <br />
                                            <br />
                                            <br />
                                            关于：<br />
                                            <br />
                                            <font size="+1" style="font-weight: bold">Database Tools - 数据库工具(ASP.NET版)</font><br />
                                            版本：3.0<br />
                                            更新时间：2016/01/04<br />
                                            <br />
                                            VBE Studio © 2015-2016 邠心工作室 版权所有
                                        </div>
                                    </div>
                                </asp:Panel>
                            </div>
                        </div>
                    </div>
                </div>
            </ContentTemplate>
        </asp:UpdatePanel>
    </div>
    </form>
</body>
</html>
