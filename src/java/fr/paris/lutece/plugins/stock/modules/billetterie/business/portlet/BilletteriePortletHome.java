package fr.paris.lutece.plugins.stock.modules.billetterie.business.portlet;

import fr.paris.lutece.portal.business.portlet.IPortletInterfaceDAO;
import fr.paris.lutece.portal.business.portlet.PortletHome;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;


public class BilletteriePortletHome extends PortletHome
{

    // Static variable pointed at the DAO instance
    private static IBilletterieDAO _dao = (IBilletterieDAO) SpringContextService.getPluginBean( "billetterie",
            "BilletteriePortletDAO" );

    /* This class implements the Singleton design pattern. */
    private static BilletteriePortletHome _singleton = null;

    /**
     * Constructor
     */
    public BilletteriePortletHome( )
    {
        // if ( _singleton == null )
        // {
        // _singleton = this;
        // }
    }

    public String getPortletTypeId( )
    {

        String strCurrentClassName = this.getClass( ).getName( );
        String strPortletTypeId = PortletTypeHome.getPortletTypeId( strCurrentClassName );

        return strPortletTypeId;
    }

    public IPortletInterfaceDAO getDAO( )
    {
        return _dao;

    }

    public static PortletHome getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = new BilletteriePortletHome( );
        }

        return _singleton;
    }
}
