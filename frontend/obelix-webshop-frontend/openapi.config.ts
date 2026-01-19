import {GeneratorConfig} from 'ng-openapi';

export const config: GeneratorConfig = {
    input: './swagger.json',
    output: './src/api',
    options: {
        generateServices: true, dateType: 'Date', enumStyle: 'enum', generateEnumBasedOnDescription: true,
    }
}
