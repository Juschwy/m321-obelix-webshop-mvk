import {GeneratorConfig} from 'ng-openapi';

export const config: GeneratorConfig = {
    input: './swagger.json', // or './swagger.json'
    output: './src/api',
    options: {
        generateServices: true, dateType: 'Date', enumStyle: 'enum', generateEnumBasedOnDescription: true,
    }
}
//
// const environment = process.env['ENV'] || 'development';
// const swaggerUrl = environment === 'production' ? 'https://api.prod.com/swagger/v1/swagger.json' : 'http://localhost:5000/swagger/v1/swagger.json';
// export const config: GeneratorConfig = {input: swaggerUrl, output: './src/api'};
