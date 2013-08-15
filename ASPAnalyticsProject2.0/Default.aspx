<%@ Page Title="Home Page" Language="C#" MasterPageFile="~/Site.master" AutoEventWireup="true"
    CodeFile="Default.aspx.cs" Inherits="_Default" Buffer="true" %>
<%@ Import Namespace="System.Drawing" %> 

<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
    <script type="text/javascript"> 
function toggle(showTable, hiddenTable) {
    var ele = document.getElementById(showTable);
    //var text = document.getElementById(hiddenTable).value;
	if(ele.style.display == "none") {
	    ele.style.display = "block";
	    //text.innerHTML = "Collapse Table";
	    //alert(text);
  	}
	else {
		ele.style.display = "none";
		//text.innerHTML = "Expand Table";
	}
}

function DisplayList(status) {
    //var dimensionStatus = document.getElementById('collapseCheckBox').checked;
    var dimensionStatus = status;
    if (dimensionStatus == true) {
        document.getElementById('expandGridView1').style.display = "none";
        //DisplayList(status);
    } else {
        document.getElementById('expandGridView1').style.display = "block";
    }
}

</script>

<%--<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"
   type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('.toggler').click(function () {
            if (this.checked) {
                $('div#expandGridView1').slideUp();
            } else {
                $('div#expandGridView1').slideDown();
            }
        });
    })
</script>--%>


    <style type="text/css">
        .style2
        {
            width:70%;
            height: 35px;
            padding-top:5px;
        }

        .TextBox
        {
            width: 90%;            
        }
        .WrapStyle
        {
            word-break: break-all;
        }
        .style3
        {
            width: 20%;
            height: 35px;
            padding-top:5px;
        }
        .style4
        {
            width: 10%;
            height: 35px;
            padding-top:5px;
        }

    </style>
</asp:Content>
<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">
    <form id="form1" action="" style="float: left; width: 1260px; height:100%;">
    <div style="float: left; width: 20%; height: 100%;">
        <asp:Panel runat="server" id="table_button">
          <table style="float:left; width: 100%; height:100%;">
             <tr>
                 <td>
                     <asp:Button ID="LoadTreeButton" runat="server" onclick="LoadTreeButton_Click" 
                        Text="Load Tree View" />
                 </td>
                 <td>
                    <asp:Button ID="RunJob" runat="server" onclick="RunJob_Click" OnClientClick="return confirm('Are you sure you want to update? It may take a while to process.');"
                        Text="Update Data" />                     
                 </td>
              </tr>
              <tr>  
                <td align="left" colspan="2"><asp:Label ID="JobMessage" runat="server" Text="Message"></asp:Label></td>
              </tr>
              <tr>
                 <td colspan="2" align="left" valign="top" >
                    <asp:TreeView ID="PageTreeView" runat="server" OnSelectedNodeChanged="PageTreeView_SelectedNodeChanged"  >
                    </asp:TreeView>
                 </td>
             </tr>
          </table>
        </asp:Panel>
    <br />
    <br />
 
    </div>

    <div style="float: left; width: 80%; height: 100%">
        <asp:Panel runat="server" id="Panel2">
          <table style="float:left; width: 1000px; height:100%;">
             <tr>
                 <td align="left" valign="top" style="padding-left:10px;" class="style2" >
                 <div style="float: left; width:100%; ">
                    <div runat="server" style="float: left; width:25%;">
                        <asp:CheckBox runat="server" ID="PageShowCheckBox" Checked="false" AutoPostBack="True" OnCheckedChanged="PageShowDiv" Text="Show or Hide Page Table" />
                    </div> 
                    <div style="float: left; width:30%;">    
                        <asp:TextBox ID="pageSearchTextBox" runat="server" CssClass="TextBox" ></asp:TextBox>                 
                    </div>
                    <div style="float:left; width:40%;">
                        <asp:Button ID="pageSearchButton" runat="server" onclick="pageSearchButton_Click" 
                         Text="Page Search" />
                     &nbsp
                     <asp:Label ID="Label5" runat="server" Text="Message" ForeColor="red" >(xxx.asp)</asp:Label>
                    </div>

                    <div style="padding-top: 10px; float:left; width: 100%">
                        <div style=" float:left; width: 25%">
                            <asp:Label ID="Label6" runat="server" ForeColor="red" Text="Message">Start Date:</asp:Label>
                            &nbsp
                            <asp:TextBox ID="StartDateTextBox" runat="server" ></asp:TextBox>  
                        </div>               
                        <div style=" float:left; width: 25%">
                            <asp:Label ID="Label7" runat="server" Text="Message" ForeColor="red" >End Date:</asp:Label>
                            &nbsp
                            <asp:TextBox ID="EndDateTextBox" runat="server" ></asp:TextBox> 
                        </div>
                        <div style=" float:left; width: 20%; padding-left: 5px;">
                        
                            <asp:DropDownList ID="BindDateDropDownList" runat="server" Font-Bold="True" ForeColor="#006666" 
                           AutoPostBack="True">
                                                        
                                <asp:ListItem Value="0">-- Select --</asp:ListItem>
                                <asp:ListItem Value="RSPageCreateDate">Created Date</asp:ListItem>
                                <asp:ListItem Value="RSPageModifiedDate">Modified Date</asp:ListItem>
                                <asp:ListItem Value="RSPageLastAccessDate">Last Access Date</asp:ListItem>
                                
                            </asp:DropDownList>
                        
                        </div>
                        <div style=" float:left; width: 28%">
                            <div>
                                <asp:Button ID="DateSearchButton" runat="server" Text="Date Search" 
                                    onclick="DateSearchButton_Click" />
                                &nbsp
                                <asp:Button ID="AnalyticsButton" runat="server" Text="Google Analytics" 
                                    onclick="AnalyticsButton_Click" />

                            </div>
                        </div>
                        
                        <div style="float:left; width: 100%">
                        <asp:Label ID="Label8" runat="server" Text="Label" ForeColor="red" ></asp:Label>
                        </div>
                    </div>
                   </div>
                 </td>

             </tr>

             <tr>
             
                <td colspan="3" style="width:1000px; " >

                    <div id="expandGridView1" runat="server" class="expandGridView1" style="display: block; float:left; width:100%; " >
                      <div style="float:left; width: 75%;">
                       <asp:GridView ID="PageGridView" runat="server" 
                        OnPageIndexChanging="PageGridView_PageIndexChanging" AllowPaging="True" 
                            OnSelectedIndexChanged="PageGridView_SelectedIndexChanged"  
                             PageSize="15" AutoGenerateColumns="False" 
                            BorderStyle="Double"  BorderColor="#DF5015" Width="100%"
                         >
                             <Columns>                                   
                                 <asp:CommandField ShowSelectButton="True" />
                                 <asp:BoundField DataField="PageURL" HeaderText="Page URL" >
                                 <ItemStyle HorizontalAlign="Left" VerticalAlign="Top" />
                                 </asp:BoundField>  

                                 <asp:BoundField DataField="RSPageCreateDate" HeaderText="Created Date" >
                                 <ItemStyle HorizontalAlign="Left" VerticalAlign="Top" />
                                 </asp:BoundField>  
                                 
                                 <asp:BoundField DataField="RSPageModifiedDate" HeaderText="Modified Date" >
                                 <ItemStyle HorizontalAlign="Left" VerticalAlign="Top" />
                                 </asp:BoundField> 
                                 
                                 <asp:BoundField DataField="RSPageLastAccessDate" HeaderText="Access Date" >
                                 <ItemStyle HorizontalAlign="Left" VerticalAlign="Top" />
                                 </asp:BoundField>                 
                             </Columns>
                            <SelectedRowStyle BackColor="Orange" />
                            <HeaderStyle BackColor="#df5015" Font-Bold="true" ForeColor="White" />
                             <RowStyle BackColor="#E1E1E1"  />
                             <AlternatingRowStyle BackColor="White"  />
                             

                        <PagerSettings Mode="NumericFirstLast" FirstPageText="First" 
                            LastPageText="Last" NextPageText="Next" PreviousPageText="Previous" />

                     </asp:GridView>
                        <div style="float:left; width:70%; height:35px; vertical-align:top;">                          
                         <asp:Label ID="Label9" runat="server" Text="Label" ForeColor="red"></asp:Label> 
                        </div>  
                     </div> 
                     
                     <div style="float:left; width: 20%; padding-left: 10px; padding-bottom: 10px; Height:390px; vertical-align:top;">                                              
                            <div style="  padding-bottom:10px; width:100%;" >   
                                <div style=" padding-top:10px; padding-bottom:10px;">
                                    <asp:TextBox ID="pageURLTextBox" runat="server" CssClass="TextBox"></asp:TextBox>
                                </div>             
                                <div style="float:left;">
                                   <asp:ListBox ID="KeywordListBox" runat="server" Height="60px" 
                                        SelectionMode="Multiple" Rows="4" Font-Bold="True" ForeColor="#006666"></asp:ListBox>
                                </div>
                                <div style="float:left; padding-top:10px;">
                                    <asp:Button ID="keywordSearchButton" runat="server" Text="Keyword Search" 
                                        onclick="keywordSearchButton_Click" />
                                </div>
                                <div style="float:left; padding-top:10px; padding-bottom:10px;">
                                    <asp:Button ID="DeleteKeywordButton" runat="server" Text="Delete Keyword" 
                                        onclick="DeleteKeywordButton_Click" OnClientClick="return confirm('Are you sure you want to delete the keywords?');" />
                                </div>
                            </div>
                                               
                            <div style=" padding-top:10px; padding-bottom:10px; " >   
                                <div style="padding-top:10px;">
                                    <asp:TextBox ID="addKeywordTextBox" runat="server" CssClass="TextBox"></asp:TextBox>
                                </div>                  
                                <div>
                                    <asp:Label ID="addKeywordLabel" runat="server" ForeColor="red" >Input Keyword (Necessary)</asp:Label>
                                </div>
                         
                            <div style="padding-top:10px;">                     
                                <asp:TextBox ID="addKeywordDescriptionTextBox" runat="server" CssClass="TextBox"></asp:TextBox>
                                <div>
                                    <asp:Label ID="addKeywordDescriptionLabel" runat="server" ForeColor="red" >Input Description (Not Necessary)</asp:Label>
                                </div>
                            </div>
                        
                            <div style="padding-top:10px; ">
                                <asp:Button ID="addKeywordButton" runat="server" Text="Add Keyword" 
                                    onclick="addKeywordButton_Click" />
                            </div>
                              
                            <div style="padding-top:10px">
                                <asp:Button ID="updateKeywordButton" runat="server" 
                                    onclick="updateKeywordButton_Click" 
                                    OnClientClick="return confirm('Are you sure you want to update the keyword? It may take a while to process.');" 
                                    Text="Update Keyword" />
                            </div>
                        </div>
                       </div>  
                       
                      </div> 
               
                </td>
                

             </tr>

             <tr>
                <td>
                    <div style="float:left; width:100%; vertical-align:top;">                          
                         <asp:Label ID="Label10" runat="server" Text=" " Font-Bold="true"></asp:Label> 
                          <asp:CheckBox ID="GoogleCheckBox" runat="server" 
                              AutoPostBack="True" Checked="false" OnCheckedChanged="GoogleShowDiv" 
                              Text="Show or Hide" />
                     </div> 
                     <div id="expandGridView6" runat="server" style="display: block; float:left; width:100%;" >
                     <div style="float: left; width:100%; padding-bottom:10px;">
                        <asp:GridView ID="AnalyticsGridView" runat="server"                       
                         BorderStyle="Double"  BorderColor="#DF5015" GridLines="None" Width="100%" 
                         AutoGenerateColumns="false" >
                         <Columns>                                                                   
                                 <asp:BoundField DataField="PagePath" HeaderText="Page URL" >
                                 <ItemStyle HorizontalAlign="Left" VerticalAlign="Top" />
                                 </asp:BoundField>  

                                 <asp:BoundField DataField="Date" HeaderText="Visited Date" >
                                 <ItemStyle HorizontalAlign="Left" VerticalAlign="Top" />
                                 </asp:BoundField>  
                                 
                                 <asp:BoundField DataField="PageView" HeaderText="Page Views" >
                                 <ItemStyle HorizontalAlign="Left" VerticalAlign="Top" />
                                 </asp:BoundField> 
                                 
                                 <asp:BoundField DataField="UniquePageViews" HeaderText="UniquePageViews" >
                                 <ItemStyle HorizontalAlign="Left" VerticalAlign="Top" />
                                 </asp:BoundField>  
                                 
                                 <asp:BoundField DataField="AvgTimeOnPage" HeaderText="AvgTimeOnPage" >
                                 <ItemStyle HorizontalAlign="Left" VerticalAlign="Top" />
                                 </asp:BoundField>                
                             </Columns>

                         <HeaderStyle BackColor="#df5015" Font-Bold="true" ForeColor="White" HorizontalAlign="Left" />
                         <RowStyle BackColor="#E1E1E1" />
                         <AlternatingRowStyle BackColor="White" />
                        </asp:GridView>
                    </div> 
                    </div>
                </td>
             
             </tr>


             <tr >
                
                 <td align="left" class="style2" colspan="3">
                  <%--<a id="collapseGridView2" 
                         href="javascript:toggle('expandGridView2','collapseGridView2');">--%>
                    <div style="float:left; width:100%;">
                        <asp:Label ID="Label2" runat="server" Text=" " Font-Bold="true">
                        </asp:Label>   
                        <asp:CheckBox runat="server" ID="PageAttributeShowCheckBox" AutoPostBack="True" Checked="false" Text="Show or Hide" OnCheckedChanged="PageAttributeShowDiv"/>
                    </div>
                   <%-- </a>--%>
                 </td>
             </tr>
             <tr>
                <td align="center" colspan="3" >
                 <div id="expandGridView2" runat="server" style="display: block; float:left; width:100%;" >

                    <div style="float: left; width:100%;">
                        <asp:GridView ID="PageAttributeSummaryGridView" runat="server"                       
                         BorderStyle="Double"  BorderColor="#DF5015" GridLines="None" Width="100%" 
                         OnSelectedIndexChanged="PageAttributeSummerGridView_SelectedIndexChanged" AutoGenerateColumns="False"
                         AutoGenerateSelectButton="True"
                        >
                        <Columns>                                  
                            <asp:BoundField DataField="RSPageName" HeaderText="Page Name" >
                            <ItemStyle HorizontalAlign="center" VerticalAlign="Top" />
                            </asp:BoundField>
                            <asp:BoundField DataField="RSAttributeName" HeaderText="Attribute Name" >
                            <ItemStyle HorizontalAlign="center" VerticalAlign="Top" />
                            </asp:BoundField>
                            <asp:BoundField DataField="totalNumber" HeaderText="Total Number" >
                            <ItemStyle HorizontalAlign="center" VerticalAlign="Top" />
                            </asp:BoundField>
                         </Columns>
                        <SelectedRowStyle BackColor="Orange" />
                         <HeaderStyle BackColor="#df5015" Font-Bold="true" ForeColor="White" />
                         <RowStyle BackColor="#E1E1E1" />
                         <AlternatingRowStyle BackColor="White" />
                        </asp:GridView>
                     </div>
                   </div>
                </td>
             </tr>
             <tr>
                 <td align="left" class="style2" colspan="3">
                 <%--<a id="collapseGridView3" 
                         href="javascript:toggle('expandGridView3','collapseGridView3');">--%>
                    <%--<asp:Label ID="Label4" runat="server" Text=" " Font-Bold="true">
                    </asp:Label>  --%>
                    <asp:CheckBox runat="server" ID="PageAttributeAffiliationCheckBox" AutoPostBack="True" Checked="false" Text="Show or Hide" OnCheckedChanged="PageAttributeAffiliationShowDiv"/>
                    <%--</a>--%>
                 </td>
             </tr>

             <tr>
                <td align="center" colspan="3" >
                 <div id="expandGridView3" runat="server" style="display: block; float:left; width:100%;" >
                    <asp:GridView ID="PageAttributeAffiliationGridView" runat="server" AutoGenerateColumns="False"
                         OnPageIndexChanging="PageAttributeAffiliationGridView_PageIndexChanging" AllowPaging="True"
                     BorderStyle="Double"  BorderColor="#DF5015" Width="100%" 
                      >
                     <Columns>                                  
                        <asp:BoundField DataField="RSPageAttributeAffiliationdescription" ItemStyle-CssClass="WrapStyle"
                             HeaderText="Description" >
                         <ItemStyle HorizontalAlign="Left" VerticalAlign="Top" />
                         </asp:BoundField>
                     </Columns>

                     <HeaderStyle BackColor="#df5015" Font-Bold="true" ForeColor="White" />
                     <RowStyle BackColor="#E1E1E1" />
                     <AlternatingRowStyle BackColor="White" />


                    <PagerSettings Mode="NumericFirstLast" FirstPageText="First" 
                        LastPageText="Last" NextPageText="Next" PreviousPageText="Previous" />
                    </asp:GridView>
                    </div>
                </td>
             </tr>

              <tr>
                 <td align="left" class="style2" colspan="3">
                 <%--<a id="collapseGridView4" 
                         href="javascript:toggle('expandGridView4','collapseGridView4');">--%>
                    <asp:Label ID="Label1" runat="server" Text=" " Font-Bold="true">
                    </asp:Label>  
                    <asp:CheckBox runat="server" ID="PageKeywordCheckBox" AutoPostBack="True" Checked="false" Text="Show or Hide" OnCheckedChanged="PageKeywordShowDiv"/>
                    <%--</a>--%>
                 </td>
             </tr>

              <tr>
                <td align="center" colspan="3" >
                 <div id="expandGridView4" runat="server" style="display: block; float:left; width:100%;" >
                    <asp:GridView ID="PageKeywordSummaryGridView" runat="server" AutoGenerateColumns="False"                        
                     BorderStyle="Double"  BorderColor="#DF5015" Width="100%" AutoGenerateSelectButton = "true"
                     OnPageIndexChanging="PageKeywordSummaryGridView_PageIndexChanging" AllowPaging="True"
                     OnSelectedIndexChanged="PageKeywordSummaryGridView_SelectedIndexChanged"
                      >
                     <Columns>
                        <asp:BoundField DataField="URL" 
                             HeaderText="Page URL" >
                         <ItemStyle HorizontalAlign="Left" VerticalAlign="Top" />
                         </asp:BoundField>
                        <asp:BoundField DataField="RSKeywordName" 
                             HeaderText="Keyword Name" >
                         <ItemStyle HorizontalAlign="Left" VerticalAlign="Top" />
                         </asp:BoundField>
                        <asp:BoundField DataField="totalNumber" 
                             HeaderText="Total Number" >
                         <ItemStyle HorizontalAlign="Left" VerticalAlign="Top" />
                         </asp:BoundField>

                     </Columns>
                     <SelectedRowStyle BackColor="Orange" />
                     <HeaderStyle BackColor="#df5015" Font-Bold="true" ForeColor="White" />
                     <RowStyle BackColor="#E1E1E1" />
                     <AlternatingRowStyle BackColor="White" />

                     <PagerSettings Mode="NumericFirstLast" FirstPageText="First" 
                        LastPageText="Last" NextPageText="Next" PreviousPageText="Previous" />
                    </asp:GridView>
                    </div>
                </td>
             </tr>


             <tr>
                 <td align="left" class="style2" colspan="3">
                 <%--<a id="collapseGridView5" 
                         href="javascript:toggle('expandGridView5','collapseGridView4');">--%>
                    <%--<asp:Label ID="Label3" runat="server" Text=" " Font-Bold="true">
                    </asp:Label>  --%>
                    <asp:CheckBox runat="server" ID="PageKeywordAffiliationCheckBox" AutoPostBack="True" Checked="false" Text="Show or Hide" OnCheckedChanged="PageKeyordAffiliationCheckBoxShowDiv"/>
                    <%--</a>--%>
                 </td>
             </tr>

              <tr>
                <td align="center" colspan="3" >
                 <div id="expandGridView5" runat="server" style="display: block; float:left; width:1000px;" >
                    <asp:GridView ID="PageKeywordAffiliationGridView" runat="server" AutoGenerateColumns="False"                        
                     BorderStyle="Double"  BorderColor="#DF5015" Width="100%" 
                     OnPageIndexChanging="PageKeywordAffiliationGridView_PageIndexChanging" AllowPaging="True"
                      >
                     <Columns>                                  
                        <asp:BoundField DataField="RSPageKeywordAffiliationdescription" ItemStyle-CssClass="WrapStyle"
                             HeaderText="Description" >
                         <ItemStyle HorizontalAlign="Left" Wrap="true" VerticalAlign="Top" Width="75%" />
                         </asp:BoundField>
                        <asp:BoundField DataField="RSPageKeywordAffiliationSector" 
                             HeaderText="Sector" >
                         <ItemStyle HorizontalAlign="Left" VerticalAlign="Top" />
                         </asp:BoundField>

                     </Columns>

                     <HeaderStyle BackColor="#df5015" Font-Bold="true" ForeColor="White" />
                     <RowStyle BackColor="#E1E1E1" />
                     <AlternatingRowStyle BackColor="White" />

                     <PagerSettings Mode="NumericFirstLast" FirstPageText="First" 
                        LastPageText="Last" NextPageText="Next" PreviousPageText="Previous" />
                    </asp:GridView>
                    </div>
                </td>
             </tr>
          </table>
        </asp:Panel>
    </div>
     </form>
</asp:Content>
