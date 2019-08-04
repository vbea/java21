<%@ Page Title="" Language="C#" MasterPageFile="~/Java.Master" AutoEventWireup="true" CodeBehind="Video.aspx.cs" Inherits="Java21.Net.Video" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>21天学通Java-视频</title>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <table style="width:100%; height:100%; margin:10px 5px;">
        <tr>
            <td style="width:20%; vertical-align:top;">
                <asp:ListBox ID="listVideo" runat="server" AutoPostBack="True" OnSelectedIndexChanged="listVideo_SelectedIndexChanged" Height="500px"></asp:ListBox>
            </td>
            <td style="width:80%; vertical-align:top;">
                <div id="panFlash" runat="server" style="width:100%; height:500px; text-align:center; padding:20px 10px;"></div>
            </td>
        </tr>
    </table>
</asp:Content>
