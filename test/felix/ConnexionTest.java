package felix;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.Before;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class ConnexionTest extends TestCase
{

    Connexion connexion;

    @Before
    public void setUp() throws Exception
    {
        this.connexion = new Connexion();
    }

    ////////////////////////////
    /// 3 tests de la methode contains3separatedDots
    ///////////////////////////

    @Test
    public void testContains3separatedDotsWithSeparatedDots()
    {
        testContains3SeparatedDots("1.1.1.1", true);
    }

    @Test
    public void testContains3separatedDotsWithStickedDots()
    {
        this.testContains3SeparatedDots("...", false);
    }

    @Test
    public void testContains3separatedDotsWithMixedDots()
    {
        testContains3SeparatedDots("...1", false);
        testContains3SeparatedDots("..1.1", false);
        testContains3SeparatedDots(".1..1", false);
        testContains3SeparatedDots("1.1..1", false);
        testContains3SeparatedDots(".1..", false);
        testContains3SeparatedDots(".1..1", false);
    }

    /**
     * Test de la methode contains3SeparatedDots
     *
     * @param testData la données de tests
     * @param attendedResult le restultat de l'assertion
     */
    protected void testContains3SeparatedDots(String testData, boolean attendedResult)
    {
        Class<?>[] methodeTypeParameters = {String.class};

        Optional<Boolean> result = introspectMethodResult(connexion, "contains3SeparatedDots", methodeTypeParameters, testData);

        if (result.isPresent())
        {
            if (attendedResult)
            {
                assertTrue(result.get());
                return;
            }

            assertFalse(result.get());
            return;
        }

        fail("Invocation de la methode contains3separatedDots Impossible");
    }

    ////////////////////////////
    /// Test de la methode isInteger
    ///////////////////////////
    @Test
    public void testIsIntegerWithOnlyInteger()
    {
        testIsInteger("1234567", true);
    }

    @Test
    public void testIsIntegerWithNoneInteger()
    {
        testIsInteger("qbcd#!@asfg", false);
    }

    @Test
    public void testIsIntegerWithMixedCharacters()
    {
        testIsInteger("122abcd", false);
        testIsInteger("122a2bc4d", false);
        testIsInteger("q122abc5dq", false);
        testIsInteger("b122abcd6", false);
    }

    /**
     * Test de la methode isInteger
     *
     * @param testData la donnée de test
     * @param attendedResult le résultat de l'assertion
     */
    protected void testIsInteger(String testData, boolean attendedResult)
    {
        Class<?>[] methodeTypeParameters = {String.class};

        Optional<Boolean> result = introspectMethodResult(connexion, "isInteger", methodeTypeParameters, testData);

        if (result.isPresent())
        {
            if (attendedResult)
            {
                assertTrue(result.get());
                return;
            }

            assertFalse(result.get());
            return;
        }

        fail("Invocation de la methode contains3separatedDots Impossible");
    }

    ////////////////////////////
    /// Test de la methode isPortValid
    /// On suppose que la methode isInteger est correctement testée
    /// Puisque l'on ne mock pas la classe que l'on veut tester
    ///////////////////////////
    @Test
    public void testIsPortValidWithNonInteger()
    {
        assertFalse(this.connexion.isPortValid("ab234"));
    }

    @Test
    public void testIsPortValidWithPortUnderLowLimit()
    {
        assertFalse(this.connexion.isPortValid("0"));
        assertFalse(this.connexion.isPortValid("-150"));
    }

    @Test
    public void testIsPortValidWithPortGreaterThanHighLimit()
    {
        assertFalse(this.connexion.isPortValid("65536"));
        assertFalse(this.connexion.isPortValid("6500000"));
    }

    @Test
    public void testIsPortValidWithGoodPort()
    {
        assertTrue(this.connexion.isPortValid("1"));
        assertTrue(this.connexion.isPortValid("65535"));
        assertTrue(this.connexion.isPortValid("150"));
        assertTrue(this.connexion.isPortValid("25"));
        assertTrue(this.connexion.isPortValid("12345"));
    }

    ////////////////////////////
    /// Test de la methode isPartOfIpValide
    /// On suppose que la methode isInteger est correctement testée
    /// Puisque l'on ne mock pas la classe que l'on veut tester
    ///////////////////////////
    @Test
    public void testIsPartOfIpValidWithNonInteger()
    {
        testPartOfIpValide("abc342", false);
    }

    @Test
    public void testIsPartOfIPValidWithPartUnderLowLimit()
    {
        testPartOfIpValide("-1", false);
        testPartOfIpValide("-114203894", false);
    }

    @Test
    public void testIsPartOfIPValidWithPortGreaterThanHighLimit()
    {
        testPartOfIpValide("256", false);
        testPartOfIpValide("323423421", false);
    }

    @Test
    public void testIsPartOfIPValidWithGoodIp()
    {
        testPartOfIpValide("0", true);
        testPartOfIpValide("255", true);
        testPartOfIpValide("12", true);
        testPartOfIpValide("123", true);
        testPartOfIpValide("254", true);
        testPartOfIpValide("234", true);
    }

    ////////////////////////////
    /// Test de la methode isIpValid
    /// On suppose que la methode isInteger est correctement testée
    /// On suppose que la medhode isPartOfIPValid est correctement testée
    /// On suppose que la methode contains3SeparatedDots est correctement testée
    /// Puisque l'on ne mock pas la classe que l'on veut tester
    ///////////////////////////
    @Test
    public void testIsIPValidWithNot3SeparatedDots()
    {
        assertFalse(this.connexion.isIPValid("..1."));
    }

    @Test
    public void testIsIPValidWithNotOnlyNumericParts()
    {
        assertFalse(this.connexion.isIPValid("a.4.1.3"));
    }

    @Test
    public void testIsIPValidWithUnderLowLimitsPart()
    {
        assertFalse(this.connexion.isIPValid("-1.4.1.3"));
        assertFalse(this.connexion.isIPValid("5.4.1.-1"));
    }

    @Test
    public void testIsIPValidWithHighLimitsParts()
    {
        assertFalse(this.connexion.isIPValid("256.4.1.3"));
        assertFalse(this.connexion.isIPValid("1.4.269.3"));

    }


    @Test
    public void testIsIPValidWithGoodIp()
    {
        assertTrue(this.connexion.isIPValid("0.0.0.0"));
        assertTrue(this.connexion.isIPValid("255.255.255.255"));
        assertTrue(this.connexion.isIPValid("123.23.23.3"));
        assertTrue(this.connexion.isIPValid("3.123.54.8"));
        assertTrue(this.connexion.isIPValid("0.255.23.4"));
    }

    /**
     * Methode utilisée pour tester avec n'importe quel parametre la methode PartOfIpValide
     *
     * @param testData La donnée de test
     * @param attendedResult Le resultat de l'assertion attendue
     */
    protected void testPartOfIpValide(String testData, boolean attendedResult)
    {
        Class<?>[] methodeTypeParameters = {String.class};

        Optional<Boolean> result = introspectMethodResult(connexion, "isPartOfIpValide", methodeTypeParameters, testData);

        if (result.isPresent())
        {
            if (attendedResult)
            {
                assertTrue(result.get());
                return;
            }

            assertFalse(result.get());
            return;
        }

        fail("Invocation de la methode contains3separatedDots Impossible");
    }

    /**
     * Permet de récupérer le résultat d'une méthode par introspection
     *
     * @param concernedObject L'object concerné
     * @param method La méthode invoké
     * @param argsMethodType Les types de la methods
     * @param realArgs Les arguments passées
     * @return
     */
    private static <T> Optional<T> introspectMethodResult(Object concernedObject, String method, Class<?>[] argsMethodType, Object... realArgs) {
        try {
            Method methode = concernedObject.getClass().getDeclaredMethod(method, argsMethodType);
            methode.setAccessible(true);
            return Optional.of((T) methode.invoke(concernedObject, realArgs));
        } catch (NoSuchMethodException e) {
            fail("Method not found");
        } catch (IllegalAccessException e) {
            fail("Illegal access");
        } catch (InvocationTargetException e) {
            fail("Invocation target exception");
        }

        return Optional.empty();
    }
}
