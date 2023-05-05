import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import RNLocationEnabler from 'react-native-instantpay-location-enabler';

export default function App() {
    const [result, setResult] = React.useState();

    React.useEffect(() => {
        
    }, []);

    const checkLocattion = async () => {

        let res = await RNLocationEnabler.checkLocation();

        console.log(res);

        //let dx = JSON.parse(res.data);

        //console.log(dx);
    }

    return (
        <View style={styles.container}>
            <Text>Result: {result}</Text>
            <Button onPress={() => checkLocattion()} title='Check Location'/>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
    box: {
        width: 60,
        height: 60,
        marginVertical: 20,
    },
});
